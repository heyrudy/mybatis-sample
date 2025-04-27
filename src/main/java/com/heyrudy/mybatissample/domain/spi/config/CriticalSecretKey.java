package com.heyrudy.mybatissample.domain.spi.config;

public sealed interface CriticalSecretKey<T>
    extends SecretKey<T>
    permits CriticalDbSecretPropertiesKey {

}