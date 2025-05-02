package com.heyrudy.mybatissample.domain.spi.config;

public sealed interface ConfigKey<T>
    extends DependencyKey<T>
    permits SecretKey,
    CriticalConfigKey {

}
