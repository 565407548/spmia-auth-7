package com.spmia.zuul.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.spmia.zuul.model.AbTestingRoute;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Component
public class SpecialRoutesFilter extends ZuulFilter {

    private static final Logger logger = LoggerFactory.getLogger(SpecialRoutesFilter.class);

    private static final int FILTER_ORDER = 1;
    private static final boolean SHOULD_FILTER = true;

    @Autowired
    FilterUtils filterUtils;


    @Autowired
    RestTemplate restTemplate;

    @Override
    public String filterType() {
        return FilterUtils.FILTER_TYPE_ROUTE;
    }

    @Override
    public int filterOrder() {
        return FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return SHOULD_FILTER;
    }

    private ProxyRequestHelper helper = new ProxyRequestHelper();

    private AbTestingRoute getAbRoutingInfo(String serviceName) {
        ResponseEntity<AbTestingRoute> restExchange;
        try {
            restExchange = restTemplate.exchange(
                    "http://specialroutes-service/v1/route/abtesting/{serviceName}",
                    HttpMethod.GET,
                    null,
                    AbTestingRoute.class,
                    serviceName);
        } catch (HttpClientErrorException ex) {
            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) return null;
            throw ex;
        }
        return restExchange.getBody();
    }

    private String buildRouteString(String oldEndpoint, String newEndpoint, String serviceName) {
        logger.info(String.format("old:%s new:%s serviceName: %s", oldEndpoint, newEndpoint, serviceName));

        int index = oldEndpoint.indexOf(serviceName);
        String strippedRoute = oldEndpoint.substring(index + serviceName.length());
        String route = String.format("%s/%s", newEndpoint, strippedRoute);
        logger.info(String.format("Target route: %s", route));
        return route;
    }

    private String getMethod(HttpServletRequest request) {
        String sMethod = request.getMethod();
        return sMethod.toUpperCase();
    }

    private HttpHost getHttpHost(URL host) {
        return new HttpHost(
                host.getHost(),
                host.getPort(),
                host.getProtocol());
    }

    private Header[] convertHeaders(MultiValueMap<String, String> headers) {
        List<Header> list = new ArrayList<>();
        for (String name : headers.keySet()) {
            for (String value : headers.get(name)) {
                list.add(new BasicHeader(name, value));
            }
        }
        return list.toArray(new BasicHeader[0]);
    }

    private HttpResponse forwardRequest(HttpClient httpclient,
                                        HttpHost httpHost,
                                        HttpRequest httpRequest) throws IOException {
        return httpclient.execute(httpHost, httpRequest);
    }


    private MultiValueMap<String, String> revertHeaders(Header[] headers) {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        for (Header header : headers) {
            String name = header.getName();
            if (!map.containsKey(name)) {
                map.put(name, new ArrayList<>());
            }
            map.get(name).add(header.getValue());
        }
        return map;
    }

    private InputStream getRequestBody(HttpServletRequest request) {
        InputStream requestEntity = null;
        try {
            requestEntity = request.getInputStream();
        } catch (IOException ex) {
            // no requestBody is ok.
        }
        return requestEntity;
    }

    private void setResponse(HttpResponse response) throws IOException {
        this.helper.setResponse(
                response.getStatusLine().getStatusCode(),
                response.getEntity() == null ? null : response.getEntity().getContent(),
                revertHeaders(response.getAllHeaders()));
    }

    private HttpResponse forward(HttpClient httpclient,
                                 String method,
                                 String uri,
                                 HttpServletRequest request,
                                 MultiValueMap<String, String> headers,
                                 MultiValueMap<String, String> params,
                                 InputStream requestEntity) throws Exception {
        Map<String, Object> info = this.helper.debug(method, uri, headers, params,
                requestEntity);
        URL host = new URL(uri);
        HttpHost httpHost = getHttpHost(host);

        HttpRequest httpRequest;
        int contentLength = request.getContentLength();
        InputStreamEntity entity = new InputStreamEntity(requestEntity, contentLength,
                request.getContentType() != null
                        ? ContentType.create(request.getContentType()) : null);
        switch (method.toUpperCase()) {
            case "POST":
                HttpPost httpPost = new HttpPost(uri);
                httpRequest = httpPost;
                httpPost.setEntity(entity);
                break;
            case "PUT":
                HttpPut httpPut = new HttpPut(uri);
                httpRequest = httpPut;
                httpPut.setEntity(entity);
                break;
            case "PATCH":
                HttpPatch httpPatch = new HttpPatch(uri);
                httpRequest = httpPatch;
                httpPatch.setEntity(entity);
                break;
            default:
                httpRequest = new BasicHttpRequest(method, uri);

        }

        httpRequest.setHeaders(convertHeaders(headers));

        return forwardRequest(httpclient, httpHost, httpRequest);
    }

    /**
     * 根据权重，随机判断是否进行 ab testing
     */
    public boolean useSpecialRoute(AbTestingRoute testRoute) {
        Random random = new Random();

        if (testRoute.getActive().equals("N")) return false;

        int value = random.nextInt((10 - 1) + 1) + 1;

        logger.info(String.format("random=%d weight=%d", value, testRoute.getWeight()));

        return testRoute.getWeight() < value;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();

        AbTestingRoute abTestRoute = getAbRoutingInfo(filterUtils.getServiceId());

        if (abTestRoute != null && useSpecialRoute(abTestRoute)) {
            String route = buildRouteString(
                    ctx.getRequest().getRequestURI(),
                    abTestRoute.getEndpoint(),
                    ctx.get("serviceId").toString());
            forwardToSpecialRoute(route);
        }

        return null;
    }

    private void forwardToSpecialRoute(String route) {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();

        MultiValueMap<String, String> headers = this.helper
                .buildZuulRequestHeaders(request);
        MultiValueMap<String, String> params = this.helper
                .buildZuulRequestQueryParams(request);
        String method = getMethod(request);
        InputStream requestEntity = getRequestBody(request);
        if (request.getContentLength() < 0) {
            context.setChunkedRequestBody();
        }

        this.helper.addIgnoredHeaders();
        CloseableHttpClient httpClient = null;
        HttpResponse response;

        try {
            httpClient = HttpClients.createDefault();
            response = forward(httpClient, method, route, request, headers,
                    params, requestEntity);
            setResponse(response);
            logger.info(response.getEntity().toString());
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException ex) {
            }
        }
    }
}
