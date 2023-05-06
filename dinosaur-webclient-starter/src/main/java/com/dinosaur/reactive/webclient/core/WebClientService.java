package com.dinosaur.reactive.webclient.core;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author wanghu
 */
public class WebClientService {
    private final LoadBalancerClient loadBalancerClient;

    public WebClientService(final LoadBalancerClient loadBalancerClient){
        this.loadBalancerClient = loadBalancerClient;
    }

    public String getPath(String serviceId){
        ServiceInstance instance = loadBalancerClient.choose(serviceId);
        instance.getHost();
        instance.getPort();
        WebClient.create();
        return  "";
    }
}
