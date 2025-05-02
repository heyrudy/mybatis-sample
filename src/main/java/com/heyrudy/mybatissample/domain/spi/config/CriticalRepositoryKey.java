package com.heyrudy.mybatissample.domain.spi.config;

public sealed interface CriticalRepositoryKey<T>
    extends DependencyKey<T>
    permits CityRepositoryKey {

}