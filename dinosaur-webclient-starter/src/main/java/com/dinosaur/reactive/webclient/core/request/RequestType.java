package com.dinosaur.reactive.webclient.core.request;

/**
 * @author wanghu
 */
public enum RequestType {
    GET(GetRequest.class.getName()),
    POST(PostRequest.class.getName()),
    PUT(""),
    DELETE("");

    String value;

    RequestType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
