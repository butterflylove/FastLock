package com.destiny.lock.client.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.client.RestTemplate;

public abstract class AbstractRestClient implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(AbstractRestClient.class);
    private RestTemplate restTemplate;

    private int connectTimeout = 500;
    private int readTimeout = 10000;
    private int maxConnections = 1024;
    private int maxConnectionsPerRoute = 256;
    private String charset = "UTF-8";
    private boolean compress = true;
    private boolean connectionReuse = true;
    private boolean automaticRetry = true;

    public void init() {
        if (this.restTemplate == null) {
            AdvancedRestTemplate restTemplate = new AdvancedRestTemplate();
            restTemplate.setCharset(charset);
            restTemplate.setCompress(compress);
            restTemplate.setConnectTimeout(connectTimeout);
            restTemplate.setReadTimeout(readTimeout);
            restTemplate.setMaxConnections(maxConnections);
            restTemplate.setMaxConnectionsPerRoute(maxConnectionsPerRoute);
            restTemplate.setAutomaticRetry(automaticRetry);
            restTemplate.setConnectionReuse(connectionReuse);
            restTemplate.init();
            this.restTemplate = restTemplate;
        }
    }

    @Override
    public void afterPropertiesSet() {
        init();
    }

    protected <T> T postForObject(String serviceUrl, Object param, Class<T> responseClass, Object... uriVariables) {
        return restTemplate.postForObject(serviceUrl, param, responseClass, uriVariables);
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public AbstractRestClient setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        return this;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public AbstractRestClient setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public AbstractRestClient setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public AbstractRestClient setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
        return this;
    }

    public int getMaxConnectionsPerRoute() {
        return maxConnectionsPerRoute;
    }

    public AbstractRestClient setMaxConnectionsPerRoute(int maxConnectionsPerRoute) {
        this.maxConnectionsPerRoute = maxConnectionsPerRoute;
        return this;
    }

    public String getCharset() {
        return charset;
    }

    public AbstractRestClient setCharset(String charset) {
        this.charset = charset;
        return this;
    }

    public boolean isCompress() {
        return compress;
    }

    public AbstractRestClient setCompress(boolean compress) {
        this.compress = compress;
        return this;
    }

    public boolean isConnectionReuse() {
        return connectionReuse;
    }

    public AbstractRestClient setConnectionReuse(boolean connectionReuse) {
        this.connectionReuse = connectionReuse;
        return this;
    }

    public boolean isAutomaticRetry() {
        return automaticRetry;
    }

    public AbstractRestClient setAutomaticRetry(boolean automaticRetry) {
        this.automaticRetry = automaticRetry;
        return this;
    }
}
