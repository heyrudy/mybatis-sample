package com.heyrudy.mybatissample.domain.spi.config;

public sealed interface CriticalRepositoryKey<T>
    extends ServiceKey<T>
    permits CityRepositoryKey {

}