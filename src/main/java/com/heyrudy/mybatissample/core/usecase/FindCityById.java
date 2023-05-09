package com.heyrudy.mybatissample.core.usecase;

import com.heyrudy.mybatissample.core.spi.dbstorage.ICityStore;
import com.heyrudy.mybatissample.core.model.City;

import java.util.Optional;

public class FindCityById {

    private final ICityStore store;

    public FindCityById(ICityStore store) {
        this.store = store;
    }


    public Optional<City> execute(long id) {
        return store.findCityById(id);
    }
}
