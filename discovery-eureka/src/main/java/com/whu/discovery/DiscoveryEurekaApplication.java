package com.whu.discovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableEurekaServer
@SpringBootApplication
public class DiscoveryEurekaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiscoveryEurekaApplication.class, args);
	}

	@Configuration
	@EnableWebSecurity
	public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.csrf().disable();
			http.authorizeRequests().anyRequest().authenticated().and().httpBasic();
		}
	}
}
