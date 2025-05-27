package com.heyrudy.mybatissample.context;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(
    value = {
        H2DbSecret.class
    }
)
public class DbConfiguration {

}
