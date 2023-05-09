package com.heyrudy.mybatissample.core.spi.dbstorage;

import com.heyrudy.mybatissample.core.model.City;

import java.util.List;
import java.util.Optional;

public interface ICityStore {

    City save(City city);
    Optional<City> findCityById(long id);
    List<City> findCities();
}
