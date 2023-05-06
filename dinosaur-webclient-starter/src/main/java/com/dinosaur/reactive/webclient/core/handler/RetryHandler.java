package com.dinosaur.reactive.webclient.core.handler;

import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.util.retry.Retry;

public class RetryHandler extends Retry {
    @Override
    public Publisher<?> generateCompanion(Flux<RetrySignal> flux) {
        return null;
    }
}
