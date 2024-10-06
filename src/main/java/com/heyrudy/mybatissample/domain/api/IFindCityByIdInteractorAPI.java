package com.heyrudy.mybatissample.domain.api;

import com.heyrudy.mybatissample.domain.model.city.FullCity;
import com.heyrudy.mybatissample.domain.model.common.CityCriteriaDTO;
import com.heyrudy.mybatissample.domain.model.error.CityNotFoundError;
import io.vavr.control.Either;

public interface IFindCityByIdInteractorAPI {

    /**
     * @param cityCriteriaDTO city's id to fetch from the database
     * @return Required information about a city
     */
    Either<CityNotFoundError, FullCity> execute(final CityCriteriaDTO cityCriteriaDTO);
}
