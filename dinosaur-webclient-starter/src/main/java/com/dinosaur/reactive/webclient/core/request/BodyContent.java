package com.dinosaur.reactive.webclient.core.request;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersUriSpec;

/**
 * @author wanghu
 *
 */
public interface BodyContent<T> {

//    ResponseSpec requestBody(RequestHeadersUriSpec requestHeadersUriSpec, MediaType mediaType);
    ResponseSpec requestBody(T requestHeadersUriSpec, RpcContext rpcContext);
}
