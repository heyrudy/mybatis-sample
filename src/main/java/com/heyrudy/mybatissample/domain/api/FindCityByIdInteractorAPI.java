package com.heyrudy.mybatissample.domain.api;

import static java.lang.String.format;

import com.heyrudy.mybatissample.domain.model.city.City;
import com.heyrudy.mybatissample.domain.model.common.CityCriteriaDTO;
import com.heyrudy.mybatissample.domain.model.error.CityNotFoundError;
import com.heyrudy.mybatissample.domain.spi.ICityDbSPI;
import io.vavr.control.Either;
import io.vavr.control.Option;

public class FindCityByIdInteractorAPI {

    private final ICityDbSPI db;

    public FindCityByIdInteractorAPI(ICityDbSPI db) {
        this.db = db;
    }

    /**
     * @param cityCriteriaDTO city's id to fetch from the database
     * @return Required information about a city
     */
    public Either<CityNotFoundError, City> findCityById(final CityCriteriaDTO cityCriteriaDTO) {
        return Option.ofOptional(db.findCityById(cityCriteriaDTO.cityId()))
            .toEither(() -> new CityNotFoundError(
                format("City with %d was not found", cityCriteriaDTO.cityId())));

    }
}
