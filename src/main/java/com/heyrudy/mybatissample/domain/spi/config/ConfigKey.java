package com.heyrudy.mybatissample.domain.spi.config;

public sealed interface ConfigKey<T>
    extends ServiceKey<T>
    permits CriticalConfigKey,
    CriticalConfigLocatorKey {

}
