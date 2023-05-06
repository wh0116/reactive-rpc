package com.whu.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class ReativeGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReativeGatewayApplication.class, args);
	}

}
