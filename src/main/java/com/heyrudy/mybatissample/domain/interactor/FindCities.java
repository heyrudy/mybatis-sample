package com.heyrudy.mybatissample.domain.interactor;

import com.heyrudy.mybatissample.domain.spi.ICityDbSPI;
import com.heyrudy.mybatissample.domain.model.city.City;

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
