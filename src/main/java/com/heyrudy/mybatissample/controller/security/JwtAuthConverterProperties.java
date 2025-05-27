package com.heyrudy.mybatissample.controller.security;

//@ConfigurationProperties(prefix = "jwt.auth.converter")
public record JwtAuthConverterProperties(String principalAttribute, String resourceId) {

}