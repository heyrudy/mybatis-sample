package com.heyrudy.mybatissample.core.interactor;

import com.heyrudy.mybatissample.core.spi.store.ICityDbSPI;
import com.heyrudy.mybatissample.core.model.city.City;

import java.util.List;

public class FindCities {

    private final ICityDbSPI db;

    public FindCities(ICityDbSPI db) {
        this.db = db;
    }

    public List<City> execute() {
        return db.findCities();
    }
}
