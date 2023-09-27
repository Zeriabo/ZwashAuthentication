package com.zwash.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.zwash.common.repository")
public class JpaConfig {
    // Any additional configuration you may need.
}
