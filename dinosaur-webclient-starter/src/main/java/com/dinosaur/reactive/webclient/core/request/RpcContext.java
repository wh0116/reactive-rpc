package com.dinosaur.reactive.webclient.core.request;

import java.util.List;
import java.util.Map;

/**
 * @author whu
 * @param <T> returned Type of calling rpc method
 */
public class RpcContext<T> {
    private RequestType requestEnum;
    private ParameterEnum parameterEnum;
    private String contextPath;
    private String uri;
    private List<String> headers;
    private String consumer;
    private String producer;
    private Map data;
    private boolean isFlux = false;
    private Class<T> resultType;

    public boolean isFlux() {
        return isFlux;
    }

    public void setFlux(boolean flux) {
        isFlux = flux;
    }

    public Class<T> getResultType() {
        return resultType;
    }

    public void setResultType(Class<T> resultType) {
        this.resultType = resultType;
    }

    public String getContextPath() {
        return contextPath;
    }

    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public ParameterEnum getParameterEnum() {
        return parameterEnum;
    }

    public void setParameterEnum(ParameterEnum parameterEnum) {
        this.parameterEnum = parameterEnum;
    }

    public RequestType getRequestEnum() {
        return requestEnum;
    }

    public void setRequestEnum(RequestType requestEnum) {
        this.requestEnum = requestEnum;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public String getConsumer() {
        return consumer;
    }

    public void setConsumer(String consumer) {
        this.consumer = consumer;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public Map getData() {
        return data;
    }

    public void setData(Map data) {
        this.data = data;
    }
}
