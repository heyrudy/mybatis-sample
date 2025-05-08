package com.heyrudy.mybatissample.context;

public sealed interface SecretKey<T>
    extends ConfigKey<T>
    permits CriticalSecretKey {

}
