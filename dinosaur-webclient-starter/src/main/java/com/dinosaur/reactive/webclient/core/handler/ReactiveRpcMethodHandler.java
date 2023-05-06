package com.dinosaur.reactive.webclient.core.handler;

import com.dinosaur.reactive.webclient.core.request.RpcBuilder;
import com.dinosaur.reactive.webclient.core.request.RpcContext;

import java.util.Map;

/**
 * @author wanghu
 * @since 2023/4/3
 */
public class ReactiveRpcMethodHandler implements RpcMethodHandler{

//    private final String contextPath;
//    private final String url;
    private final RpcContext rpcContext;
    private RpcBuilder rpcBuilder;

    public ReactiveRpcMethodHandler(RpcContext rpcContext) {
        this.rpcContext = rpcContext;
        rpcBuilder= new RpcBuilder.Builder().rpcContext(rpcContext).builder();
    }

    @Override
    public Object invoke(Map data) throws Throwable {
        /**
         *发起RPC请求，并解析RPC服务的响应结果返回
         */
        this.rpcContext.setData(data);
        return rpcBuilder.request();
    }
}
