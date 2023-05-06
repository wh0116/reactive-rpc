package com.dinosaur.reactive.webclient.core.configuration;

import com.dinosaur.reactive.webclient.common.SpringUtils;
import com.dinosaur.reactive.webclient.core.loadbalancer.RandomLoadBalancerClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.reactive.ReactorLoadBalancerExchangeFilterFunction;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebClientConfiguration {

    @Autowired
    private ReactorLoadBalancerExchangeFilterFunction reactorLoadBalance;

    @Bean
    ReactorServiceInstanceLoadBalancer multiRouteLoadBalancer(
            ObjectProvider<ServiceInstanceListSupplier> serviceInstanceListSupplierObjectProvider) {
        return new RandomLoadBalancerClient(serviceInstanceListSupplierObjectProvider);
    }

    @Bean
    HRpcConfiguration hrpcConfiguration() {
        return new HRpcConfiguration();
    }

//    @Bean
//    public WebClient webClient() {
//        return WebClient.builder()
//                .filter(reactorLoadBalance).build();
//    }

    @Bean
    public SpringUtils springUtils(){
        return new SpringUtils();
    }

//    @Bean
//    @Scope("prototype")
//    public GetRequest getRequest() {
//        return new GetRequest(reactorLoadBalance);
//    }

//    @Bean
//    public RequestFactory requestFactory(final ApplicationContext applicationContext) {
//        return new RequestFactory(applicationContext);
//    }

//    @Bean
//    public RequestFacade requestFacade(RequestFactory requestFactory) {
//        return new RequestFacade(requestFactory);
//    }
}
