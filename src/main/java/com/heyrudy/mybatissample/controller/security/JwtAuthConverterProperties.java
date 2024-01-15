package com.heyrudy.mybatissample.controller.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt.auth.converter")
public record JwtAuthConverterProperties(String principalAttribute, String resourceId) {

}
