package com.dinosaur.reactive.webclient.core.request;

//import org.springframework.core.type.MethodMetadata;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * 解析RPC服务的method注解及参数注解，并设置rpc上下文
 * @author wanghu
 */
public class RpcAnnotationParse {

    private RpcContext rpcContext;

    public RpcAnnotationParse() {
        this.rpcContext = new RpcContext();
    }

    public RpcContext getRpcContext() {
        return this.rpcContext;
    }

    public RpcContext parseAnnotationOnClass(Method method) {
        RequestMapping requestMapping = AnnotatedElementUtils.findMergedAnnotation(method,RequestMapping.class);
        //设置请求类型GET or POST
        rpcContext.setRequestEnum(RequestType.valueOf(requestMapping.method()[0].name()));
        //设置url
        String value = requestMapping.value()[0];
        if(StringUtils.hasText(value)){
            rpcContext.setUri(value);
        }else {
            String path = requestMapping.path()[0];
            if (StringUtils.hasText(path)){
                rpcContext.setUri(path);
            }
        }
        //设置producer
        Arrays.stream(requestMapping.produces())
                .findFirst()
                .ifPresent(produce->rpcContext.setProducer(produce));

        //设置consumer
        Arrays.stream(requestMapping.consumes())
                .findFirst()
                .ifPresent((consumer)->rpcContext.setConsumer(consumer));

        //设置headers
        List<String> headers = new ArrayList<String>(8);
        for (String header : requestMapping.headers()) {
            headers.add(header);
        }
        if(headers.size()>0){
            rpcContext.setHeaders(headers);
        }else {
            headers = null;
        }
        //判断返回类型Flux还是Mono
        boolean isFlux = method.getReturnType().isAssignableFrom(Flux.class);
        rpcContext.setFlux(isFlux);
        //获取实际返回类型
        Class elementType = extractElementType(method.getGenericReturnType());
        rpcContext.setResultType(elementType);
         return rpcContext;
    }

    /**
     * 解析参数注解
     * @param method
     * @param args
     * @return
     */
    public Map parseParamsOfMethod(Method method, Object[] args) {
        Parameter[] parameters = method.getParameters();
        Map map = new LinkedHashMap(4);
        MultiValueMap<String, Object> formValue = new LinkedMultiValueMap<>(4);
        for(int i=0;i<parameters.length;i++) {
            if(parameters[i].isAnnotationPresent(PathVariable.class)){
                map.put(parameters[i].getName(),args[i]);
            }else if (parameters[i].isAnnotationPresent(RequestBody.class)) {
                map.put(parameters[i].getName(),args[i]);
            } else if (parameters[i].isAnnotationPresent(RequestParam.class)) {
                formValue.add(parameters[i].getName(), args[i]);
            }
//            map.put(parameters[i], args[i]);
        }
        return map.isEmpty() ? formValue : map;
    }

    /**
     * 提取method返回类型
     * @param genericReturnType
     * @return
     * @param <T>
     */
    private <T> Class<T> extractElementType(Type genericReturnType) {
        Type[] actualTypeArguments = ((ParameterizedType) genericReturnType).getActualTypeArguments();
        for (Type t : actualTypeArguments) {
            if (t instanceof Class) {
                return (Class<T>) t;
            } else {
                Type[] aaa = ((ParameterizedType) t).getActualTypeArguments();
                return (Class<T>) ((ParameterizedType) t).getRawType();
            }
        }
        return (Class<T>) actualTypeArguments[0];
    }
}
