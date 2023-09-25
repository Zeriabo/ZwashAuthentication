package com.zwash.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("con.zwash.auth.repository")
@SpringBootApplication
public class ZwashAuthenticationApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZwashAuthenticationApplication.class, args);
	}

}
