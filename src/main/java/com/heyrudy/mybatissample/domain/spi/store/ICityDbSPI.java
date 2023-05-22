package com.heyrudy.mybatissample.domain.spi.store;

import com.heyrudy.mybatissample.domain.model.city.City;

import java.util.List;
import java.util.Optional;

public interface ICityDbSPI {

    City save(City city);
    Optional<City> findCityById(long id);
    List<City> findCities();
}
