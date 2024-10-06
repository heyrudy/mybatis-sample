package com.heyrudy.mybatissample.domain.model.city;

public interface ICityFactory {

    ICity create(long id, String name, String state, String country);
}
