package com.dinosaur.reactive.webclient.core.request;

import com.dinosaur.reactive.webclient.core.exception.ServiceException;
import io.netty.channel.ConnectTimeoutException;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.net.ConnectException;
import java.time.Duration;

/**
 * @author wanghu
 */
public class WebClientContext {

    private final RequestStrategy requestStrategy;

    public WebClientContext(RequestStrategy requestStrategy) {
        this.requestStrategy = requestStrategy;
    }

    public WebClient request(RpcContext rpcContext) {
        return this.requestStrategy.request(rpcContext);
    }

    public WebClient.ResponseSpec requestBody(WebClient.RequestHeadersUriSpec requestHeadersUriSpec, RpcContext context) {
        return this.requestStrategy.requestBody(requestHeadersUriSpec, context);
    }

    public Retry retryStrategy() {
        return Retry.backoff(3, Duration.ofSeconds(5))
                .filter(throwable -> throwable instanceof ServiceException)
//                .filter(throwable -> throwable instanceof ConnectTimeoutException)
                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
                    throw new ServiceException("External Service failed to process after max retries", HttpStatus.SERVICE_UNAVAILABLE.value());
                });
    }
}
