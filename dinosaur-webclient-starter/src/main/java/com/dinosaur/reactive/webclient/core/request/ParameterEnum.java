package com.dinosaur.reactive.webclient.core.request;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author wanghu
 */
public enum ParameterEnum {
    PathVariable("org.springframework.web.bind.annotation.PathVariable"),
    RequestBody("org.springframework.web.bind.annotation.RequestBody"),
    RequestParam("org.springframework.web.bind.annotation.RequestParam");

    String value;

    ParameterEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
