package com.heyrudy.mybatissample.application.context;

public sealed interface CriticalSecretKey<T>
    extends SecretKey<T>
    permits CriticalDbSecretKey
    , CriticalRestSecretKey {

}
