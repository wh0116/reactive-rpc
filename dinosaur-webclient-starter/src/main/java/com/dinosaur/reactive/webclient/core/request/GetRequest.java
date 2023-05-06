package com.dinosaur.reactive.webclient.core.request;

import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import java.util.LinkedHashMap;

/**
 * @author wanghu
 */
public class GetRequest implements RequestStrategy<WebClient.RequestHeadersUriSpec>{
//    private ReactorLoadBalancerExchangeFilterFunction reactorLoadBalance;
//
//    public GetRequest(ReactorLoadBalancerExchangeFilterFunction reactorLoadBalance) {
//        this.reactorLoadBalance = reactorLoadBalance;
//    }

    @Override
    public WebClient request(RpcContext context) {
        return WebClient.builder()
                .baseUrl(context.getContextPath())
                .filter((request, next) -> loadBalanceFilter(request, next))
                .build();
    }


    @Override
    public WebClient.ResponseSpec requestBody(WebClient.RequestHeadersUriSpec requestHeadersUriSpec, RpcContext context) {
        if (context.getData() instanceof LinkedHashMap) {
            return requestHeadersUriSpec.uri(context.getUri(),context.getData())
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve();
        } else {
            return requestHeadersUriSpec.uri(uriBuilder -> ((UriBuilder)uriBuilder)
                            .path(context.getUri())
                            .queryParams((MultiValueMap<String, String>) context.getData())
                            .build())
//                .accept(MediaType.MULTIPART_FORM_DATA)
                    .retrieve();
        }

    }

}
