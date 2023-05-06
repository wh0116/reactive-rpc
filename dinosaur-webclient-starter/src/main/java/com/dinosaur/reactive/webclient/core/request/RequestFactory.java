package com.dinosaur.reactive.webclient.core.request;

import org.springframework.context.ApplicationContext;
import org.springframework.util.ClassUtils;

/**
 * Factory创建request strategy
 * @author wanghu
 */
public class RequestFactory {

//    private final ApplicationContext applicationContext;

//    public RequestFactory(ApplicationContext applicationContext) {
//        this.applicationContext = applicationContext;
//    }

    public static RequestStrategy request(RequestType requestType) {
        RequestStrategy  requestStrategy = null;
        try {
            requestStrategy = (RequestStrategy) Class.forName(requestType.getValue())
                    .getDeclaredConstructor()
                    .newInstance();
//            requestStrategy = (RequestStrategy) applicationContext.getBean(requestType.getValue());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return  requestStrategy;
    }
}
