package com.zwash.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication(scanBasePackages = {"com.zwash.auth", "com.zwash.common"})
@EnableJpaRepositories(basePackages = "com.zwash.common.repository")
@EntityScan(basePackages = "com.zwash.common.pojos")
public class ZwashAuthenticationApplication {

	public static void main(String[] args) {
		
		SpringApplication.run(ZwashAuthenticationApplication.class, args);
	  
	}
	
}
		