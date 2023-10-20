package com.zwash.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@ComponentScan(basePackages = {"com.zwash.common","com.zwash.auth"})
@SpringBootApplication
@EnableDiscoveryClient
public class ZwashAuthenticationApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZwashAuthenticationApplication.class, args);
	}

}
