package com.zwash.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan("com.zwash.common.pojos")
@ComponentScan({"com.zwash.auth", "com.zwash.common","com.zwash.booking"})
@SpringBootApplication
public class ZwashAuthenticationApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZwashAuthenticationApplication.class, args);
	}

}
