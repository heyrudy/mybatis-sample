package com.heyrudy.mybatissample.domain.spi.config;

public sealed interface CriticalDbSPIKey<T>
    extends DependencyKey<T>
    permits CityDbSPIKey {

}