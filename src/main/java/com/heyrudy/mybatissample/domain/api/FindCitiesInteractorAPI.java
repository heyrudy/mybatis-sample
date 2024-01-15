package com.heyrudy.mybatissample.domain.api;

import com.heyrudy.mybatissample.domain.model.city.City;
import com.heyrudy.mybatissample.domain.spi.ICityDbSPI;
import java.util.List;

public class FindCitiesInteractorAPI {

    private final ICityDbSPI db;

    public FindCitiesInteractorAPI(ICityDbSPI db) {
        this.db = db;
    }

    /**
     * @return All cities fetch from the database
     */
    public List<City> findCities() {
        return db.findCities();
    }
}
