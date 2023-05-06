package com.dinosaur.reactive.webclient.core;

import org.springframework.cloud.context.named.NamedContextFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.support.GenericApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wanghu
 */
public class WebClientFactory extends NamedContextFactory<WebClientSpecification> {


    public WebClientFactory() {
        super(WebClientsConfiguration.class, "spring.cloud.webclient", "spring.cloud.webclient.client.name");
    }
}
