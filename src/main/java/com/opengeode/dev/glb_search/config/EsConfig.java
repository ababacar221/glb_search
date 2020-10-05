package com.opengeode.dev.glb_search.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class EsConfig {

    private static final Logger LOG = LoggerFactory.getLogger(EsConfig.class);

    @Value("${EsHost}")
    private String EsHost;

    @Value("${EsPort}")
    private int EsPort;

    private RestHighLevelClient restHighLevelClient;

    @Bean
    public RestHighLevelClient createInstance() {
        return buildClient();
    }

    private RestHighLevelClient buildClient() {
        try {
            restHighLevelClient = new RestHighLevelClient(
                    RestClient.builder(
                            new HttpHost(EsHost, EsPort, "http"),
                            new HttpHost(EsHost, EsPort+1, "http")));
        } catch (Exception e) {
            LOG.error(e.getMessage());
            throw e;
        }
        return restHighLevelClient;
    }
}
