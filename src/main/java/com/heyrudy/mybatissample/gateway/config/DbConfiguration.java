package com.heyrudy.mybatissample.gateway.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(
    value = {
        DbSecretProperties.class
    }
)
public class DbConfiguration {

}
