package com.spmia.esservice.config;

/**
 * @description:
 * @author: Zheng Jim
 * @date: 19-2-19 上午11:54
 */

import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.Node;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/***
 ** 配置ES,支持集群
 */
@Slf4j
@Configuration
public class ElasticConfigration {

    //    @Value("${elasticsearch.host}")
    private String esHost;

    //    @Value("${elasticsearch.port}")
    private int esPort;

    //    @Value("${elasticsearch.clusterName}")
    private String esClusterName;

    private RestHighLevelClient client;

    private void init() {
        esHost = "127.0.0.1";
        esPort = 9200;
        esClusterName = "elasticsearch";
    }

    @PostConstruct
    public void initialize() throws Exception {
        init();

//        Settings esSettings = Settings.builder()
//                .put("cluster.name", esClusterName)
//                .put("client.transport.sniff", true).build();
//        client = new PreBuiltTransportClient(esSettings);
//
//        String[] esHosts = esHost.trim().split(",");
//        for (String host : esHosts) {
//            client.addTransportAddress(new TransportAddress(
//                    InetAddress.getByName(host),
//                    esPort));            client.close();
//        }

        RestClientBuilder builder = RestClient.builder(
                new HttpHost(esHost, esPort));
//        Header[] defaultHeaders = new Header[]{new BasicHeader("header", "value")};
//        builder.setDefaultHeaders(defaultHeaders);
//        builder.setMaxRetryTimeoutMillis(10000);
//        builder.setFailureListener(new RestClient.FailureListener() {
//            @Override
//            public void onFailure(Node node) {
//                super.onFailure(node);
//            }
//        });


        client = new RestHighLevelClient(builder);
    }

    @Bean
    public RestHighLevelClient client() {
        return client;
    }

    @PreDestroy
    public void destroy() {
        if (client != null) {
            try {
                client.close();
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }
}
