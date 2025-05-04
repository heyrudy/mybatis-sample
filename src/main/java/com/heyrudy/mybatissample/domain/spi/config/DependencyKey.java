package com.heyrudy.mybatissample.domain.spi.config;

public sealed interface DependencyKey<T>
    extends EnvironmentKey<T>
    permits ConfigKey,
    CriticalRepositoryKey,
    NonCriticalSPIKey {

}
