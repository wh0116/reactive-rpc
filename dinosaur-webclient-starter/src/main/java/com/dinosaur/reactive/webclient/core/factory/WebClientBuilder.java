package com.dinosaur.reactive.webclient.core.factory;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @author wanghu
 */
public class WebClientBuilder {

    private final WebClient webClient;

    public WebClientBuilder(){
         webClient = WebClient.create();
    }

    private WebClient create(){
        WebClient.create();

        return null;
    }

    private WebClient create(String baseUrl){
        WebClient.create(baseUrl);
        return null;
    }

//    private WebClient.Builder builder(String baseUrl){
//        return WebClient.builder()
//                .baseUrl(baseUrl).defaultCookie("").build();
//    }

    private WebClient.Builder setMaxInMemorySize(Integer maxInMemorySize) {
        return WebClient.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(maxInMemorySize));
    }

    private WebClient.Builder setTimeout(){
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000) //connection timeout
                .responseTimeout(Duration.ofMillis(2000)) //response timeout
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS)) //read timeout
                                .addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS))); //write timeout

        return WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient));
    }

//    private WebClient.ResponseSpec get(){
//        return webClient.get()
//                .uri("/persons/{id}", id)
//                .accept(MediaType.APPLICATION_JSON)
//                .retrieve();
//    }

//    private <T>WebClient.ResponseSpec post(Class<T> t) {
//        return webClient.post()
//                .uri("/persons/{id}/{name}", id,name)
//                .contentType(MediaType.APPLICATION_JSON)
//                .bodyValue(t)
//                .retrieve();
//    }

    private <T>WebClient.ResponseSpec post() {
        return null;//webClient.post().uri("");
    }

//    private <T>Mono<T> bodyToMono(Class<T> t) {
//        return get().bodyToMono(t);
//    }
//
//    private <T> Flux<T> bodyToFlux(Class<T> t) {
//        return get().bodyToFlux(t);
//    }

}
