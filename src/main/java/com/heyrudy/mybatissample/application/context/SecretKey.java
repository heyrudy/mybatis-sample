package com.heyrudy.mybatissample.application.context;

public sealed interface SecretKey<T>
    extends ConfigKey<T>
    permits CriticalSecretKey {

}