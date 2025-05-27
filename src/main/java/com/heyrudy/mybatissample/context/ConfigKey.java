package com.heyrudy.mybatissample.context;

public sealed interface ConfigKey<T>
    extends DependencyKey<T>
    permits SecretKey,
    CriticalConfigKey {

}