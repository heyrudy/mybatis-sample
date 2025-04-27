package com.heyrudy.mybatissample.domain.spi.config;

public sealed interface DbCriticalServiceKey<T>
    extends ServiceKey<T>
    permits CityDbSPIKey {

}