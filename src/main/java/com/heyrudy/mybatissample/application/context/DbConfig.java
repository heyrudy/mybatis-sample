package com.heyrudy.mybatissample.application.context;

import com.heyrudy.mybatissample.application.context.IDbSecret.H2DbSecret;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(
    value = {
        H2DbSecret.class
    }
)
public class DbConfig {

}
