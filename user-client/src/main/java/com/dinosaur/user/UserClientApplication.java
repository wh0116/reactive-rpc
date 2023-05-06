package com.dinosaur.user;

import com.dinosaur.reactive.webclient.core.annotation.EnableHRpc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
//@LoadBalancerClient(name = "user-service",configuration = RandomLoadBalancerClient.class)
@EnableHRpc
@SpringBootApplication
public class UserClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserClientApplication.class, args);

	}

}
