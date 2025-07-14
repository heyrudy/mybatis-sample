package com.heyrudy.mybatissample.application.context;

public sealed interface CriticalRepositoryKey<T>
    extends DependencyKey<T>
    permits CityRepositoryKey {

}