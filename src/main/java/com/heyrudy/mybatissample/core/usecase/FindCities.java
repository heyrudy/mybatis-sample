package com.heyrudy.mybatissample.core.usecase;

import com.heyrudy.mybatissample.core.spi.dbstorage.ICityStore;
import com.heyrudy.mybatissample.core.model.City;

import java.util.List;

public class FindCities {

    private final ICityStore store;

    public FindCities(ICityStore store) {
        this.store = store;
    }

    public List<City> execute() {
        return store.findCities();
    }
}
