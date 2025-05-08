package com.heyrudy.mybatissample.domain.model.city;

public sealed interface ICity
    permits FullCity,
    PartialCityProxy,
    NullCity {

    long getId();

    void setId(long id);

    String getName();

    String getState();

    String getCountry();
}
