package com.heyrudy.mybatissample.application.context;

public sealed interface ConfigKey<T>
    extends DependencyKey<T>
    permits SecretKey,
    CriticalConfigKey {

}