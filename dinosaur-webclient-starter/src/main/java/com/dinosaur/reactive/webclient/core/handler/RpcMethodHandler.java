package com.dinosaur.reactive.webclient.core.handler;

import java.util.Map;

/**
 * @author wanghu
 * @since 2023/4/3
 */
public interface RpcMethodHandler {

    /**
     * 组装URL,执行Webclient远程调用，响应结果封装成对象
     * @param data RPC方法的参数
     * @return 响应结果
     * @throws Throwable
     */
    Object invoke(Map data) throws Throwable;
}
