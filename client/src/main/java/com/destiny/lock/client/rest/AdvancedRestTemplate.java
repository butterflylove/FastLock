package com.destiny.lock.client.rest;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.NoConnectionReuseStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.web.client.RestTemplate;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class AdvancedRestTemplate extends RestTemplate {
    private int connectTimeout = 5000;
    private int readTimeout = 10000;
    private int maxConnections = 100;
    private int maxConnectionsPerRoute = 20;
    private String charset = "UTF-8";
    private boolean compress = true;
    private boolean connectionReuse = true;
    private boolean automaticRetry = false;

    @SuppressWarnings("rawtypes")
    public void init() {
        Assert.isTrue(ClassUtils.isPresent("com.fasterxml.jackson.databind.ObjectMapper", RestTemplate.class.getClassLoader())
            && ClassUtils.isPresent("com.fasterxml.jackson.core.JsonGenerator", RestTemplate.class.getClassLoader()),
                "JacksonJson2 NOT found!!");
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();

        messageConverters.add(new ByteArrayHttpMessageConverter());
        messageConverters.add(new StringHttpMessageConverter(Charset.forName(charset)));
        messageConverters.add(new ResourceHttpMessageConverter());
        messageConverters.add(new SourceHttpMessageConverter<>());

        super.setMessageConverters(messageConverters);

        HttpClientBuilder builder = HttpClientBuilder.create();
        builder.setMaxConnTotal(maxConnections).setMaxConnPerRoute(maxConnectionsPerRoute);
        if (!connectionReuse) {
            builder.setConnectionReuseStrategy(NoConnectionReuseStrategy.INSTANCE);
        }
        if (!automaticRetry) {
            builder.disableAutomaticRetries();
        }
        if (!compress) {
            builder.disableContentCompression();
        }
        HttpClient httpClient = builder.build();

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(readTimeout);
        requestFactory.setConnectTimeout(connectTimeout);
        super.setRequestFactory(requestFactory);
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public AdvancedRestTemplate setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public AdvancedRestTemplate setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public AdvancedRestTemplate setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
        return this;
    }

    public int getMaxConnectionsPerRoute() {
        return maxConnectionsPerRoute;
    }

    public AdvancedRestTemplate setMaxConnectionsPerRoute(int maxConnectionsPerRoute) {
        this.maxConnectionsPerRoute = maxConnectionsPerRoute;
        return this;
    }

    public String getCharset() {
        return charset;
    }

    public AdvancedRestTemplate setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    public boolean isCompress() {
        return compress;
    }

    public AdvancedRestTemplate setCompress(boolean compress) {
        this.compress = compress;
        return this;
    }

    public boolean isConnectionReuse() {
        return connectionReuse;
    }

    public AdvancedRestTemplate setConnectionReuse(boolean connectionReuse) {
        this.connectionReuse = connectionReuse;
        return this;
    }

    public boolean isAutomaticRetry() {
        return automaticRetry;
    }

    public AdvancedRestTemplate setAutomaticRetry(boolean automaticRetry) {
        this.automaticRetry = automaticRetry;
        return this;
    }
}
