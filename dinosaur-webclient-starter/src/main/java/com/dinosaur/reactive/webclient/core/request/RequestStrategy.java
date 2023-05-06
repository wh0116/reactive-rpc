package com.dinosaur.reactive.webclient.core.request;

import com.dinosaur.reactive.webclient.common.SpringUtils;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * @author wanghu
 * @param <T>
 */
public interface RequestStrategy<T> {

    WebClient request(RpcContext context);
    WebClient.ResponseSpec requestBody(T requestHeadersUriSpec, RpcContext context);

    /**
     * 负载均衡器，支持service-name，域名，ip等
     * @param request
     * @param next
     * @return Mono<ClientResponse>
     */
    default Mono<ClientResponse> loadBalanceFilter(ClientRequest request, ExchangeFunction next) {
        URI uri = request.url();
        String serviceName = uri.getHost();
        if (serviceName.matches(String.format("^%s|%s$", HostEnum.IP_REGEX.getValue(), HostEnum.DOMAIN_REGEX.getValue())) || "localhost".equals(serviceName)) {
            return next.exchange(request);
        }
        return next.filter(SpringUtils.getBean(ReactorLoadBalancerExchangeFilterFunction.class)).exchange(request);
    }

//    WebClient.ResponseSpec requestBody(WebClient.RequestHeadersUriSpec requestHeadersUriSpec, RpcContext context);

//    WebClient contentType(MediaType mediaType);
//
//    WebClient requestBody(BodyInserters bodyInserters);
//
//    <T>WebClient requestBody(T t);
//
//    <T>WebClient requestBody(T ...ts);

//    default WebClient.Builder filter(ExchangeFilterFunction filter) {
//        Assert.notNull(filter, "ExchangeFilterFunction must not be null");
//        this.initFilters().add(filter);
//        return this;
//    }
}
