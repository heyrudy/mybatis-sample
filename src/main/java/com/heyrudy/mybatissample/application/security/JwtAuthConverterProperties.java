package com.heyrudy.mybatissample.application.security;

//@ConfigurationProperties(prefix = "jwt.auth.converter")
public record JwtAuthConverterProperties(String principalAttribute, String resourceId) {

}