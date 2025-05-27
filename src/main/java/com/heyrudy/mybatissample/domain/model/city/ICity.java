package com.heyrudy.mybatissample.domain.model.city;

public sealed interface ICity
    permits FullCity,
    PartialCityProxy,
    NullCity {

    Long getId();

    void setId(Long id);

    String getName();

    String getState();

    String getCountry();
}