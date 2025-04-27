package com.heyrudy.mybatissample.domain.spi.config;

public sealed interface CriticalConfigLocatorKey<T>
    extends ConfigKey<T>
    permits CriticalAppScopedConfigLocatorKey {

}
