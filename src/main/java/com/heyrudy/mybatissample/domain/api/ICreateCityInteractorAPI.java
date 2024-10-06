package com.heyrudy.mybatissample.domain.api;

import com.heyrudy.mybatissample.domain.model.city.FullCity;

public interface ICreateCityInteractorAPI {

    /**
     * @param fullCity city with all its details to persist in the database
     * @return Persisted city in the database
     */
    FullCity execute(final FullCity fullCity);
}
