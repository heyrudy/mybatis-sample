package com.heyrudy.mybatissample.api.service;

import com.heyrudy.mybatissample.api.controller.dto.CityCriteriaDTO;
import com.heyrudy.mybatissample.api.service.error.CityNotFoundError;
import com.heyrudy.mybatissample.core.model.City;
import com.heyrudy.mybatissample.core.usecase.CreateCity;
import com.heyrudy.mybatissample.core.usecase.FindCities;
import com.heyrudy.mybatissample.core.usecase.FindCityById;
import io.vavr.control.Either;
import io.vavr.control.Option;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.String.format;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class CityServiceAPI {

    CreateCity createCity;

    FindCityById findCityById;

    FindCities findCities;

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
