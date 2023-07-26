package com.heyrudy.mybatissample.domain.service;

import com.heyrudy.mybatissample.domain.spi.ICityDbSPI;
import com.heyrudy.mybatissample.domain.model.city.City;

public class CreateCity {

    private final ICityDbSPI db;

    public CreateCity(ICityDbSPI db) {
        this.db = db;
    }

    public City execute(City city) {
        return db.save(city);
    }
}
