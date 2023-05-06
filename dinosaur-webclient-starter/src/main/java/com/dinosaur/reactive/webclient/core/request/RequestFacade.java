package com.dinosaur.reactive.webclient.core.request;

import com.dinosaur.reactive.webclient.core.exception.ServiceException;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * 构建Webclient请求
 * @author wanghu
 */
public class RequestFacade {

//    private RpcContext rpcContext;

    /**
     * 发起rpc调用
     * 请求类型、data、header
     * consumer、producer
     * @param rpcContext RpcContext
     * @return T
     */
    public static <T> T request(final RpcContext rpcContext) {
        //RPC请求策略
        RequestStrategy requestStrategy = RequestFactory.request(getRequestType(rpcContext));
        WebClientContext context =  new WebClientContext(requestStrategy);
        //构建Webclient
        WebClient client = context.request(rpcContext);
        //组装body
        WebClient.ResponseSpec responseSpec = null;
        if(RequestType.GET.name().equals(rpcContext.getRequestEnum().name())) {
            responseSpec = context.requestBody(client.get(), rpcContext);
        } else if (RequestType.POST.name().equals(rpcContext.getRequestEnum().name())) {
            responseSpec = context.requestBody(client.post(), rpcContext);

        }
        //异常处理
        responseSpec.onStatus(httpStatus -> !httpStatus.is2xxSuccessful(),resp->{
            return Mono.error(new ServiceException(resp.statusCode().value() + "," + resp.statusCode().getReasonPhrase(),resp.rawStatusCode()));
        });
        if (!rpcContext.isFlux()) {
            return (T) responseSpec.bodyToMono(rpcContext.getResultType())
                    .retryWhen(context.retryStrategy());
        }else {
            return (T) responseSpec.bodyToFlux(rpcContext.getResultType())
                    .retryWhen(context.retryStrategy());
        }

    }

    private static RequestType getRequestType(RpcContext rpcContext){
        Assert.notNull(rpcContext.getRequestEnum(),"rpc request type is null!");
        return rpcContext.getRequestEnum();
    }

//    private static <T> T retyStategy(T t) {
//        if(t instanceof Flux) {
//            return (T) ((Flux<?>) t).retryWhen(Retry.backoff(3, Duration.ofSeconds(2)).jitter(0.75));
//        }else if (t instanceof Mono) {
//            return (T) ((Mono<?>) t).retryWhen(Retry.backoff(3, Duration.ofSeconds(2)).jitter(0.75));
//        }
//    }


}
