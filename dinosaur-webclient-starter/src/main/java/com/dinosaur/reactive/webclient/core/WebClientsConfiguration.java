package com.dinosaur.reactive.webclient.core;

import org.springframework.context.annotation.Configuration;

/**
 * @author wanghu
 */
@Configuration(proxyBeanMethods = false)
public class WebClientsConfiguration {

    private Integer maxInMemorySize;
}
