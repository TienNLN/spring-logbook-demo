package com.tiennln.springlogbookokhttpdemo.configs;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.httpclient.LogbookHttpRequestInterceptor;
import org.zalando.logbook.httpclient.LogbookHttpResponseInterceptor;

/**
 * @author TienNLN on 11/01/2023
 */
@Configuration
public class RestTemplateConfig {

    @Autowired
    private Logbook logbook;

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        CloseableHttpClient closeableHttpClient = HttpClientBuilder.create()
                .addInterceptorFirst(new LogbookHttpRequestInterceptor(logbook))
                .addInterceptorLast(new LogbookHttpResponseInterceptor())
                .build();
        HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory(closeableHttpClient);

        restTemplate.setRequestFactory(httpRequestFactory);

        return restTemplate;
    }
}
