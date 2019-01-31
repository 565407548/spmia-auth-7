package com.spmia.zuul.filters;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.spmia.zuul.model.AbTestingRoute;
import com.spmia.zuul.remote.AbTestRemote;
import okhttp3.*;
import okhttp3.internal.http.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.StreamUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Component
public class SpecialRoutesFilter extends ZuulFilter {

    private static final Logger logger = LoggerFactory.getLogger(SpecialRoutesFilter.class);

    private static final int SIMPLE_HOST_ROUTING_FILTER_ORDER = 100;
    private static final int FILTER_ORDER = 99;
    private static final boolean SHOULD_FILTER = true;

    @Autowired
    FilterUtils filterUtils;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private ProxyRequestHelper helper;

    @Autowired
    AbTestRemote abTestRemote;

    @Override
    public String filterType() {
        return FilterUtils.FILTER_TYPE_ROUTE;
    }

    @Override
    public int filterOrder() {
        return SIMPLE_HOST_ROUTING_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
//        return RequestContext.getCurrentContext().getRouteHost() != null
//                && RequestContext.getCurrentContext().sendZuulResponse();
        return true;
    }

//    private AbTestingRoute getAbRoutingInfo(String serviceName) {
//        ResponseEntity<AbTestingRoute> restExchange;
//        try {
//            restExchange = restTemplate.exchange(
//                    "http://specialroutes-service/v1/route/abtesting/{serviceName}",
//                    org.springframework.http.HttpMethod.GET,
//                    null,
//                    AbTestingRoute.class,
//                    serviceName);
//        } catch (HttpClientErrorException ex) {
//            if (ex.getStatusCode() == HttpStatus.NOT_FOUND) {
//                return null;
//            }
//            throw ex;
//        }
//        return restExchange.getBody();
//    }

    private AbTestingRoute getAbRoutingInfo(String serviceName) {
        return abTestRemote.getAbTestRoute(serviceName);
    }

    private String buildRouteString(String oldEndpoint, String newEndpoint, String serviceName) {
        logger.info(String.format("old:%s new:%s serviceName: %s", oldEndpoint, newEndpoint, serviceName));

        int index = oldEndpoint.indexOf(serviceName);
        String strippedRoute = oldEndpoint.substring(index + serviceName.length());
        String route = String.format("%s%s", newEndpoint, strippedRoute);
        logger.info(String.format("Target route: %s", route));
        return route;
    }


    /**
     * 根据权重，随机判断是否进行 ab testing
     */
    public boolean useSpecialRoute(AbTestingRoute testRoute) {
        Random random = new Random();

        if (testRoute.getActive()==0) {
            return false;
        }

        int value = random.nextInt((10 - 1) + 1) + 1;

        logger.info(String.format("random=%d weight=%d", value, testRoute.getWeight()));

        return testRoute.getWeight() < value;
    }

    @Override
    public Object run() {
        try {
            RequestContext context = RequestContext.getCurrentContext();
            AbTestingRoute abTestRoute = getAbRoutingInfo(filterUtils.getServiceId());

            if (abTestRoute != null && useSpecialRoute(abTestRoute)) {


                OkHttpClient httpClient = new OkHttpClient.Builder().build();

                HttpServletRequest request = context.getRequest();

                String uri = buildRouteString(
                        context.getRequest().getRequestURI(),
                        abTestRoute.getEndpoint(),
                        context.get("serviceId").toString());
//                String uri = this.helper.buildZuulRequestURI(request);

                String method = request.getMethod();


                Headers.Builder headers = new Headers.Builder();
                Enumeration<String> headerNames = request.getHeaderNames();
                while (headerNames.hasMoreElements()) {
                    String name = headerNames.nextElement();
                    Enumeration<String> values = request.getHeaders(name);

                    while (values.hasMoreElements()) {
                        String value = values.nextElement();
                        headers.add(name, value);
                    }
                }

                InputStream inputStream = request.getInputStream();

                RequestBody requestBody = null;
                if (inputStream != null && HttpMethod.permitsRequestBody(method)) {
                    MediaType mediaType = null;
                    if (headers.get("Content-Type") != null) {
                        mediaType = MediaType.parse(headers.get("Content-Type"));
                    }
                    requestBody = RequestBody.create(mediaType, StreamUtils.copyToByteArray(inputStream));
                }

                Request.Builder builder = new Request.Builder()
                        .headers(headers.build())
                        .url(uri)
                        .method(method, requestBody);

                Response response = httpClient.newCall(builder.build()).execute();

                LinkedMultiValueMap<String, String> responseHeaders = new LinkedMultiValueMap<>();

                for (Map.Entry<String, List<String>> entry : response.headers().toMultimap().entrySet()) {
                    responseHeaders.put(entry.getKey(), entry.getValue());
                }

                this.helper.setResponse(response.code(), response.body().byteStream(), responseHeaders);
                // prevent SimpleHostRoutingFilter from running
                context.setRouteHost(null);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
