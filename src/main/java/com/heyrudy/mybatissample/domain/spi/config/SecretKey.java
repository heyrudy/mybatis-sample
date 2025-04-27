package com.heyrudy.mybatissample.domain.spi.config;

public sealed interface SecretKey<T>
    extends ServiceKey<T>
    permits CriticalSecretKey {

}
