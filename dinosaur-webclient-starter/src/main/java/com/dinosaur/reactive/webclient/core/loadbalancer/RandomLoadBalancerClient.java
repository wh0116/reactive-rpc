package com.dinosaur.reactive.webclient.core.loadbalancer;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.*;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;

/**
 * @author wanghu
 * @since 2023/4/12
 */
public class RandomLoadBalancerClient implements ReactorServiceInstanceLoadBalancer {

    private final ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierObjectProvider;

    public RandomLoadBalancerClient(ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierObjectProvider) {
        this.serviceInstanceListSupplierObjectProvider = serviceInstanceListSupplierObjectProvider;
    }

    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        ServiceInstanceListSupplier supplier =
                serviceInstanceListSupplierObjectProvider.getIfAvailable();

        return supplier.get()
                    .next()
                    .map(this::getInstanceResponse);
    }

    private Response<ServiceInstance> getInstanceResponse(List<ServiceInstance> instances) {
        if (instances.isEmpty()) {
            return new EmptyResponse();
        }
        int size = instances.size();
        Random random = new Random();
        ServiceInstance instance = instances.get(random.nextInt(size));
        return new DefaultResponse(instance);
    }

}
