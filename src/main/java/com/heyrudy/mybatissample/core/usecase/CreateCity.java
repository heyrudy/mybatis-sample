package com.heyrudy.mybatissample.core.usecase;

import com.heyrudy.mybatissample.core.spi.dbstorage.ICityStore;
import com.heyrudy.mybatissample.core.model.City;

public class CreateCity {

    private final ICityStore store;

    public CreateCity(ICityStore store) {
        this.store = store;
    }

    public City execute(City city) {
        return store.save(city);
    }
}
