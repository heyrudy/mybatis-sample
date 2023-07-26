package com.heyrudy.mybatissample.domain.api;

import com.heyrudy.mybatissample.domain.model.common.CityCriteriaDTO;
import com.heyrudy.mybatissample.domain.model.error.CityNotFoundError;
import com.heyrudy.mybatissample.domain.model.city.City;
import com.heyrudy.mybatissample.domain.service.CreateCity;
import com.heyrudy.mybatissample.domain.service.FindCities;
import com.heyrudy.mybatissample.domain.service.FindCityById;
import io.vavr.control.Either;
import io.vavr.control.Option;

import java.util.List;

import static java.lang.String.format;

public class CityServiceAPI {

    private final CreateCity createCity;

    private final FindCityById findCityById;

    private final FindCities findCities;

    public CityServiceAPI(CreateCity createCity, FindCityById findCityById, FindCities findCities) {
        this.createCity = createCity;
        this.findCityById = findCityById;
        this.findCities = findCities;
    }

    /**
     * @param city city to persist in the database
     * @return Persisted city in the database
     */
    public City createCity(final City city) {
        return createCity.execute(city);
    }

    /**
     * @param cityCriteriaDTO city's id to fetch from the database
     * @return Required information about a city
     */
    public Either<CityNotFoundError, City> findCityById(final CityCriteriaDTO cityCriteriaDTO) {
        return Option.ofOptional(findCityById.execute(cityCriteriaDTO.cityId()))
                .toEither(() -> new CityNotFoundError(format("City with %d was not found", cityCriteriaDTO.cityId())));

    }

    /**
     * @return All cities fetch from the database
     */
    public List<City> findCities() {
        return findCities.execute();
    }
}
