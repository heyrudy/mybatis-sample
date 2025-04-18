package com.heyrudy.mybatissample.domain.model.city;

public sealed interface ICity
    permits FullCity,
    PartialCityProxy,
    NullCity {

    long getId();

    String getName();

    String getState();

    String getCountry();
}
