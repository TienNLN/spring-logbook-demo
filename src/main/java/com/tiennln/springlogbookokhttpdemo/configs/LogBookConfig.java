package com.tiennln.springlogbookokhttpdemo.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.*;

/**
 * @author TienNLN on 10/01/2023
 */
@Configuration
public class LogBookConfig {

//    @Bean
//    public Logbook configure() {
//        Logbook logbook = Logbook.builder()
//                .sink(new DefaultSink(
//                        new DefaultHttpLogFormatter(),
//                        new DefaultHttpLogWriter()
//                ))
//                .build();
//
//        return logbook;
//    }

//    @Bean
//    @ConditionalOnMissingBean(LogbookHttpRequestInterceptor.class)
//    public LogbookHttpRequestInterceptor logbookHttpRequestInterceptor(final Logbook logbook) {
//        return new LogbookHttpRequestInterceptor(logbook);
//    }
//
//    @Bean
//    @ConditionalOnMissingBean(LogbookHttpResponseInterceptor.class)
//    public LogbookHttpResponseInterceptor logbookHttpResponseInterceptor() {
//        return new LogbookHttpResponseInterceptor();
//    }

//    @Bean
//    public Logbook logbook() {
//        return Logbook.builder()
//                .sink(new DefaultSink(
//                        new DefaultHttpLogFormatter(),
//                        new DefaultHttpLogWriter()
//                ))
//                .build();
//    }

//    @Bean
//    public BodyFilter bodyFilter() {
//        return BodyFilter.merge(
//                BodyFilters.defaultValue(),
//                replaceJsonStringProperty(singleton("secret"), "XXX"));
//    }
}
