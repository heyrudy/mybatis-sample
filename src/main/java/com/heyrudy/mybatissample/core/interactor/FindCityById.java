package com.heyrudy.mybatissample.core.interactor;

import com.heyrudy.mybatissample.core.spi.store.ICityDbSPI;
import com.heyrudy.mybatissample.core.model.city.City;

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
