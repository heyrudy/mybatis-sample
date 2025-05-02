package com.heyrudy.mybatissample.domain.spi.config;

public sealed interface SecretKey<T>
    extends ConfigKey<T>
    permits CriticalSecretKey {

}
