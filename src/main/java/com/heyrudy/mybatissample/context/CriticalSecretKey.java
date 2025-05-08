package com.heyrudy.mybatissample.context;

public sealed interface CriticalSecretKey<T>
    extends SecretKey<T>
    permits CriticalDbSecretPropertiesKey {

}