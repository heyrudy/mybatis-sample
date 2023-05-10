package com.heyrudy.mybatissample.core.interactor;

import com.heyrudy.mybatissample.core.spi.store.ICityDbSPI;
import com.heyrudy.mybatissample.core.model.city.City;

public class CreateCity {

    private final ICityDbSPI db;

    public CreateCity(ICityDbSPI db) {
        this.db = db;
    }

    public City execute(City city) {
        return db.save(city);
    }
}
