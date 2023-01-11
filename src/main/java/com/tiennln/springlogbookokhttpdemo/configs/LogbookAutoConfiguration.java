//package com.tiennln.springlogbookokhttpdemo.configs;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.servlet.DispatcherType;
//import jakarta.servlet.Filter;
//import jakarta.servlet.Servlet;
//import org.apache.http.client.HttpClient;
//import org.apiguardian.api.API;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.AutoConfigureAfter;
//import org.springframework.boot.autoconfigure.condition.*;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.core.Ordered;
//import org.zalando.logbook.*;
//import org.zalando.logbook.autoconfigure.LogbookProperties;
//import org.zalando.logbook.httpclient.LogbookHttpRequestInterceptor;
//import org.zalando.logbook.httpclient.LogbookHttpResponseInterceptor;
//import org.zalando.logbook.json.JsonHttpLogFormatter;
//import org.zalando.logbook.servlet.LogbookFilter;
//import org.zalando.logbook.spring.LogbookClientHttpRequestInterceptor;
//
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//import java.util.TreeSet;
//import java.util.function.Predicate;
//
///**
// * @author TienNLN on 11/01/2023
// */
//public class LogbookAutoConfiguration {
//
//    private final LogbookProperties properties;
//
//    @API(status = API.Status.INTERNAL)
//    @Autowired
//    public LogbookAutoConfiguration(final LogbookProperties properties) {
//        this.properties = properties;
//    }
//
//    @API(status = API.Status.INTERNAL)
//    @Bean
//    @ConditionalOnMissingBean(Logbook.class)
//    public Logbook logbook(
//            final Predicate<HttpRequest> condition,
//            final CorrelationId correlationId,
//            final List<HeaderFilter> headerFilters,
//            final List<PathFilter> pathFilters,
//            final List<QueryFilter> queryFilters,
//            final List<BodyFilter> bodyFilters,
//            final List<RequestFilter> requestFilters,
//            final List<ResponseFilter> responseFilters,
//            final Strategy strategy,
//            final Sink sink) {
//
//        return Logbook.builder()
//                .condition(mergeWithExcludes(mergeWithIncludes(condition)))
//                .correlationId(correlationId)
//                .headerFilters(headerFilters)
//                .queryFilters(queryFilters)
//                .pathFilters(pathFilters)
//                .bodyFilters(bodyFilters)
//                .requestFilters(requestFilters)
//                .responseFilters(responseFilters)
//                .strategy(strategy)
//                .sink(sink)
//                .build();
//    }
//
//    private Predicate<HttpRequest> mergeWithExcludes(final Predicate<HttpRequest> predicate) {
//        return properties.getExclude().stream()
//                .map(Conditions::requestTo)
//                .map(Predicate::negate)
//                .reduce(predicate, Predicate::and);
//    }
//
//    private Predicate<HttpRequest> mergeWithIncludes(final Predicate<HttpRequest> predicate) {
//        return properties.getInclude().stream()
//                .map(Conditions::requestTo)
//                .reduce(Predicate::or)
//                .map(predicate::and)
//                .orElse(predicate);
//    }
//
//    @API(status = API.Status.INTERNAL)
//    @Bean
//    @ConditionalOnMissingBean(name = "requestCondition")
//    public Predicate<HttpRequest> requestCondition() {
//        return $ -> true;
//    }
//
//    @API(status = API.Status.INTERNAL)
//    @Bean
//    @ConditionalOnMissingBean(CorrelationId.class)
//    public CorrelationId correlationId() {
//        return new DefaultCorrelationId();
//    }
//
//    @API(status = API.Status.INTERNAL)
//    @Bean
//    @ConditionalOnMissingBean(QueryFilter.class)
//    public QueryFilter queryFilter() {
//        final List<String> parameters = properties.getObfuscate().getParameters();
//        return parameters.isEmpty() ?
//                QueryFilters.defaultValue() :
//                QueryFilters.replaceQuery(new HashSet<>(parameters)::contains, "XXX");
//    }
//
//    @API(status = API.Status.INTERNAL)
//    @Bean
//    @ConditionalOnMissingBean(HeaderFilter.class)
//    public HeaderFilter headerFilter() {
//        final Set<String> headers = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
//        headers.addAll(properties.getObfuscate().getHeaders());
//
//        return headers.isEmpty() ?
//                HeaderFilters.defaultValue() :
//                HeaderFilters.replaceHeaders(headers, "XXX");
//    }
//
//    @API(status = API.Status.INTERNAL)
//    @Bean
//    @ConditionalOnMissingBean(PathFilter.class)
//    public PathFilter pathFilter() {
//        final List<String> paths = properties.getObfuscate().getPaths();
//        return paths.isEmpty() ?
//                PathFilter.none() :
//                paths.stream()
//                        .map(path -> PathFilters.replace(path, "XXX"))
//                        .reduce(PathFilter::merge)
//                        .orElseGet(PathFilter::none);
//    }
//
//    @API(status = API.Status.INTERNAL)
//    @Bean
//    @ConditionalOnMissingBean(BodyFilter.class)
//    public BodyFilter bodyFilter() {
//        final LogbookProperties.Write write = properties.getWrite();
//        final int maxBodySize = write.getMaxBodySize();
//
//        if (maxBodySize < 0) {
//            return BodyFilters.defaultValue();
//        }
//
//        return BodyFilter.merge(BodyFilters.defaultValue(), BodyFilters.truncate(maxBodySize));
//    }
//
//    @API(status = API.Status.INTERNAL)
//    @Bean
//    @ConditionalOnMissingBean(RequestFilter.class)
//    public RequestFilter requestFilter() {
//        return RequestFilters.defaultValue();
//    }
//
//    @API(status = API.Status.INTERNAL)
//    @Bean
//    @ConditionalOnMissingBean(ResponseFilter.class)
//    public ResponseFilter responseFilter() {
//        return ResponseFilters.defaultValue();
//    }
//
//    @API(status = API.Status.INTERNAL)
//    @Bean
//    @ConditionalOnMissingBean(Strategy.class)
//    @ConditionalOnProperty(name = "logbook.strategy", havingValue = "default", matchIfMissing = true)
//    public Strategy strategy() {
//        return new DefaultStrategy();
//    }
//
//    @API(status = API.Status.INTERNAL)
//    @Bean
//    @ConditionalOnMissingBean(Strategy.class)
//    @ConditionalOnProperty(name = "logbook.strategy", havingValue = "status-at-least")
//    public Strategy statusAtLeastStrategy(@Value("${logbook.minimum-status:400}") final int status) {
//        return new StatusAtLeastStrategy(status);
//    }
//
//    @API(status = API.Status.INTERNAL)
//    @Bean
//    @ConditionalOnMissingBean(Strategy.class)
//    @ConditionalOnProperty(name = "logbook.strategy", havingValue = "body-only-if-status-at-least")
//    public Strategy bodyOnlyIfStatusAtLeastStrategy(@Value("${logbook.minimum-status:400}") final int status) {
//        return new BodyOnlyIfStatusAtLeastStrategy(status);
//    }
//
//    @API(status = API.Status.INTERNAL)
//    @Bean
//    @ConditionalOnMissingBean(Strategy.class)
//    @ConditionalOnProperty(name = "logbook.strategy", havingValue = "without-body")
//    public Strategy withoutBody() {
//        return new WithoutBodyStrategy();
//    }
//
//    @API(status = API.Status.INTERNAL)
//    @Bean
//    @ConditionalOnMissingBean(Sink.class)
//    public Sink sink(
//            @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") final HttpLogFormatter formatter,
//            final HttpLogWriter writer) {
//        return new DefaultSink(formatter, writer);
//    }
//
//    @API(status = API.Status.INTERNAL)
//    @Bean
//    @Primary
//    @ConditionalOnBean(Sink.class)
//    @ConditionalOnProperty("logbook.write.chunk-size")
//    public Sink chunkingSink(final Sink sink) {
//        return new ChunkingSink(sink, properties.getWrite().getChunkSize());
//    }
//
//    @API(status = API.Status.INTERNAL)
//    @Bean
//    @ConditionalOnMissingBean(HttpLogFormatter.class)
//    @ConditionalOnProperty(name = "logbook.format.style", havingValue = "http")
//    public HttpLogFormatter httpFormatter() {
//        return new DefaultHttpLogFormatter();
//    }
//
//    @API(status = API.Status.INTERNAL)
//    @Bean
//    @ConditionalOnMissingBean(HttpLogFormatter.class)
//    @ConditionalOnProperty(name = "logbook.format.style", havingValue = "curl")
//    public HttpLogFormatter curlFormatter() {
//        return new CurlHttpLogFormatter();
//    }
//
//    @API(status = API.Status.INTERNAL)
//    @Bean
//    @ConditionalOnMissingBean(HttpLogFormatter.class)
//    @ConditionalOnProperty(name = "logbook.format.style", havingValue = "splunk")
//    public HttpLogFormatter splunkHttpLogFormatter() {
//        return new SplunkHttpLogFormatter();
//    }
//
//    @API(status = API.Status.INTERNAL)
//    @Bean
//    @ConditionalOnBean(ObjectMapper.class)
//    @ConditionalOnMissingBean(HttpLogFormatter.class)
//    public HttpLogFormatter jsonFormatter(
//            @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection") final ObjectMapper mapper) {
//        return new JsonHttpLogFormatter(mapper);
//    }
//
//    @API(status = API.Status.INTERNAL)
//    @Bean
//    @ConditionalOnMissingBean(HttpLogWriter.class)
//    public HttpLogWriter writer() {
//        return new DefaultHttpLogWriter();
//    }
//
//    @Bean
//    @ConditionalOnMissingBean(LogbookClientHttpRequestInterceptor.class)
//    public LogbookClientHttpRequestInterceptor logbookClientHttpRequestInterceptor(Logbook logbook) {
//        return new LogbookClientHttpRequestInterceptor(logbook);
//    }
//
//    @Configuration(proxyBeanMethods = false)
//    @ConditionalOnClass({
//            HttpClient.class,
//            LogbookHttpRequestInterceptor.class,
//            LogbookHttpResponseInterceptor.class
//    })
//    static class HttpClientAutoConfiguration {
//
//        @Bean
//        @ConditionalOnMissingBean(LogbookHttpRequestInterceptor.class)
//        public LogbookHttpRequestInterceptor logbookHttpRequestInterceptor(final Logbook logbook) {
//            return new LogbookHttpRequestInterceptor(logbook);
//        }
//
//        @Bean
//        @ConditionalOnMissingBean(LogbookHttpResponseInterceptor.class)
//        public LogbookHttpResponseInterceptor logbookHttpResponseInterceptor() {
//            return new LogbookHttpResponseInterceptor();
//        }
//
//    }
//
//    @Configuration(proxyBeanMethods = false)
//    @ConditionalOnClass({
//            org.apache.hc.client5.http.classic.HttpClient.class,
//            LogbookHttpRequestInterceptor.class,
//            LogbookHttpResponseInterceptor.class
//    })
//    static class HttpClient5AutoConfiguration {
//
//        @Bean
//        @ConditionalOnMissingBean(org.zalando.logbook.httpclient5.LogbookHttpRequestInterceptor.class)
//        public org.zalando.logbook.httpclient5.LogbookHttpRequestInterceptor logbookHttpClient5RequestInterceptor(final Logbook logbook) {
//            return new org.zalando.logbook.httpclient5.LogbookHttpRequestInterceptor(logbook);
//        }
//
//        @Bean
//        @ConditionalOnMissingBean(org.zalando.logbook.httpclient5.LogbookHttpResponseInterceptor.class)
//        public org.zalando.logbook.httpclient5.LogbookHttpResponseInterceptor logbookHttpClient5ResponseInterceptor() {
//            return new org.zalando.logbook.httpclient5.LogbookHttpResponseInterceptor();
//        }
//
//    }
//
//    @Configuration(proxyBeanMethods = false)
//    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
//    @ConditionalOnClass({
//            Servlet.class,
//            LogbookFilter.class
//    })
//    static class ServletFilterConfiguration {
//
//        private static final String FILTER_NAME = "logbookFilter";
//
//        private final LogbookProperties properties;
//
//        @API(status = API.Status.INTERNAL)
//        @Autowired
//        public ServletFilterConfiguration(final LogbookProperties properties) {
//            this.properties = properties;
//        }
//
//        @Bean
//        @ConditionalOnProperty(name = "logbook.filter.enabled", havingValue = "true", matchIfMissing = true)
//        @ConditionalOnMissingBean(name = FILTER_NAME)
//        public FilterRegistrationBean logbookFilter(final Logbook logbook) {
//            final LogbookFilter filter = new LogbookFilter(logbook)
//                    .withFormRequestMode(properties.getFilter().getFormRequestMode());
////            return newFilter(filter, FILTER_NAME, Ordered.LOWEST_PRECEDENCE);
//            return null;
//        }
//
//        static FilterRegistrationBean newFilter(final Filter filter, final String filterName, final int order) {
//            @SuppressWarnings("unchecked") // as of Spring Boot 2.x
//            final FilterRegistrationBean registration = new FilterRegistrationBean(filter);
//            registration.setName(filterName);
//            registration.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ASYNC);
//            registration.setOrder(order);
//            return registration;
//        }
//
//    }
//
////    @Configuration(proxyBeanMethods = false)
////    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
////    @AutoConfigureAfter(name = {
////            "org.springframework.boot.autoconfigure.security.SecurityFilterAutoConfiguration", // Spring Boot 1.x
////            "org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration" // Spring Boot 2.x
////    })
////    static class SecurityServletFilterConfiguration {
////
////        private static final String FILTER_NAME = "secureLogbookFilter";
////
////        @Bean
////        @ConditionalOnProperty(name = "logbook.secure-filter.enabled", havingValue = "true", matchIfMissing = true)
////        @ConditionalOnMissingBean(name = FILTER_NAME)
////        public FilterRegistrationBean secureLogbookFilter(final Logbook logbook) {
////            return newFilter(new SecureLogbookFilter(logbook), FILTER_NAME, Ordered.HIGHEST_PRECEDENCE + 1);
////        }
////    }
//}