package com.heyrudy.mybatissample.context;

public sealed interface CriticalRepositoryKey<T>
    extends DependencyKey<T>
    permits CityRepositoryKey,
    MockedCityRepositoryKey {

}