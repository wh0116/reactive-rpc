package com.dinosaur.reactive.webclient.core.handler;

import com.dinosaur.reactive.webclient.core.request.RpcAnnotationParse;
import com.dinosaur.reactive.webclient.core.request.RpcContext;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Proxy;
import java.util.*;


/**
 * @author wanghu
 */
public class WebClientInvocationHandler implements InvocationHandler {

    /**
     *远程调用的分发映射：根据方法名称分发方法处理器
     *key：远程调用接口的方法反射实例
     *value：模拟的方法处理器实例
     */
    private Map<Method, RpcMethodHandler> dispatch;
    private RpcAnnotationParse rpcAnnotationParse;

    private String contextPath;

    public <T> T newInstance(Class<T> clazz, String url) {
        System.out.println("url: " + url);
        this.dispatch = new LinkedHashMap<Method, RpcMethodHandler>();

        /**
         *通过反射迭代远程调用接口的每一个方法，组装
         WebClientInvocationHandler处理器
         */
        for (Method method : clazz.getMethods()) {
            if (method.getDeclaringClass() == Object.class){
                continue;
            }
            //验证远程调用参数注解
            validateAnnotations(method);

            this.rpcAnnotationParse = new RpcAnnotationParse();
            RpcContext rpcContext = rpcAnnotationParse.parseAnnotationOnClass(method);
            //设置base url
            rpcContext.setContextPath(url);
            RpcMethodHandler handler = new ReactiveRpcMethodHandler(rpcContext);
            /**
             *重点：将方法处理器handler实例缓存到dispatch映射中
             *key为方法反射实例，value为方法处理器
             */
            this.dispatch.put(method, handler);
        }
        //创建动态代理
        T proxy = (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[] {clazz}, this);
        return proxy;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("equals".equals(method.getName())) {
            Object other = args.length > 0 && args[0] !=
                    null ? args[0] : null;
            return equals(other);
        } else if("hashCode".equals(method.getName())) {
            return hashCode();
        } else if ("toString".equals(method.getName())) {
            return toString();
        }

        Map data = rpcAnnotationParse.parseParamsOfMethod(method, args);
        RpcMethodHandler rpcMethodHandler = dispatch.get(method);
//        rpcMethodHandler.
        /**
         *方法处理器组装URL，完成RPC远程调用，并且返回Object
         结果
         */
        return rpcMethodHandler.invoke(data);
    }

    private void validateAnnotations(Method method){
        Parameter[] parameters = method.getParameters();

        String check = "";
        for (Parameter p : parameters) {
            if (p.isAnnotationPresent(PathVariable.class)) {
                if(!StringUtils.hasText(check)){
                    check = "1";
                } else if (!"1".equals(check)) {
                    throw new RuntimeException("@PathVariable cannot be used together with other!");
                }
            } else if (p.isAnnotationPresent(RequestBody.class)) {
                if(!StringUtils.hasText(check)){
                    check = "2";
                } else if (!"2".equals(check)) {
                    throw new RuntimeException("@RequestBody cannot be used together with other!");
                } else {
                    throw new RuntimeException("@RequestBody can only have one in method "+method.getName()+"("+method.getDeclaringClass()+")!");
                }
            } else if (p.isAnnotationPresent(RequestParam.class)) {
                if(!StringUtils.hasText(check)){
                    check = "3";
                } else if (!"3".equals(check)) {
                    throw new RuntimeException("@RequestParam cannot be used together with other!");
                }
            }
        }
    }
}
