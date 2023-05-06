package com.dinosaur.reactive.webclient.core.request;

import com.dinosaur.reactive.webclient.common.SpringUtils;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.LinkedHashMap;

/**
 * @author wanghu
 */
public class PostRequest implements RequestStrategy<WebClient.RequestBodyUriSpec> {
    @Override
    public WebClient request(RpcContext context) {
        return WebClient.builder()
                .baseUrl(context.getContextPath())
                .filter((request, next) -> {
//                    URI uri = request.url();
//                    String serviceName = uri.getHost();
//                    if (serviceName.matches(String.format("^%s|%s$", HostEnum.IP_REGEX.getValue(), HostEnum.DOMAIN_REGEX.getValue())) || "localhost".equals(serviceName)) {
//                        return next.exchange(request);
//                    }
//                    return next.filter(SpringUtils.getBean(ReactorLoadBalancerExchangeFilterFunction.class)).exchange(request);
                    return loadBalanceFilter(request, next);
                })
//                .filter(SpringUtils.getBean(ReactorLoadBalancerExchangeFilterFunction.class))
                .build();
    }

    @Override
    public WebClient.ResponseSpec requestBody(WebClient.RequestBodyUriSpec requestBodyUriSpec, RpcContext context) {
        if (context.getData() instanceof LinkedHashMap) {
            return requestBodyUriSpec.uri(context.getUri())
                    .accept(MediaType.APPLICATION_JSON)
                    .bodyValue(context.getData().values().toArray()[0])
                    .retrieve();
        } else {
            return requestBodyUriSpec.uri(uriBuilder -> ((UriBuilder)uriBuilder)
                            .path(context.getUri())
                            .queryParams((MultiValueMap<String, String>) context.getData())
                            .build())
//                .accept(MediaType.MULTIPART_FORM_DATA)
                    .retrieve();
        }
    }
}
