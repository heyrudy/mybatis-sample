package com.heyrudy.mybatissample.domain.spi.config;

public sealed interface CriticalConfigKey<T>
    extends ConfigKey<T>
    permits CriticalDataSourceKey,
    CriticalDSLContextKey {

}