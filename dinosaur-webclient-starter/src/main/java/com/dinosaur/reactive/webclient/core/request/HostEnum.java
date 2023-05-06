package com.dinosaur.reactive.webclient.core.request;

/**
 * @author wanghu
 */
public enum HostEnum {
    IP_REGEX("(((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?))"),
    DOMAIN_REGEX(".*\\.(com|xyz|net|top|tech|org|gov|edu|pub|cn|biz|cc|tv|info|im)");

    String value;

    HostEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
