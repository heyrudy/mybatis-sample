package com.heyrudy.mybatissample.domain.interactor;

import com.heyrudy.mybatissample.domain.spi.store.ICityDbSPI;
import com.heyrudy.mybatissample.domain.model.city.City;

import java.util.Optional;

public class FindCityById {

    private final ICityDbSPI db;

    public FindCityById(ICityDbSPI db) {
        this.db = db;
    }


    public Optional<City> execute(long id) {
        return db.findCityById(id);
    }
}
