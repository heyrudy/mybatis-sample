package com.heyrudy.mybatissample.core.spi.store;

import com.heyrudy.mybatissample.core.model.city.City;

import java.util.List;
import java.util.Optional;

public interface ICityDbSPI {

    City save(City city);
    Optional<City> findCityById(long id);
    List<City> findCities();
}
