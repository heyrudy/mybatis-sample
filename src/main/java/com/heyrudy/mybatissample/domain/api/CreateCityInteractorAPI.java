package com.heyrudy.mybatissample.domain.api;

import com.heyrudy.mybatissample.domain.model.city.City;
import com.heyrudy.mybatissample.domain.spi.ICityDbSPI;

public class CreateCityInteractorAPI {

    private final ICityDbSPI db;

    public CreateCityInteractorAPI(ICityDbSPI db) {
        this.db = db;
    }

    /**
     * @param city city to persist in the database
     * @return Persisted city in the database
     */
    public City createCity(final City city) {
        return db.save(city);
    }
}
