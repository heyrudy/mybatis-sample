package com.heyrudy.mybatissample.domain.spi;

import com.heyrudy.mybatissample.domain.model.city.FullCity;
import com.heyrudy.mybatissample.domain.model.error.CriticalRepositoryNotFoundByLocatorError;
import com.heyrudy.mybatissample.domain.spi.config.AppScopedLocator;
import cyclops.control.Reader;
import io.vavr.control.Either;
import java.util.List;
import java.util.Optional;

public interface ICityDbSPI {

    /**
     * Saves a city.
     *
     * @param fullCity The city to save
     * @return A Reader monad that either results in an error or the saved city
     */
    Reader<AppScopedLocator, Either<CriticalRepositoryNotFoundByLocatorError, FullCity>> save(
        FullCity fullCity);

    /**
     * Finds all cities.
     *
     * @return A Reader monad that either results in an error or a list of cities
     */
    Reader<AppScopedLocator, Either<CriticalRepositoryNotFoundByLocatorError, List<FullCity>>> findCities();

    /**
     * Finds a city by its ID.
     *
     * @param id The ID of the city to find
     * @return A Reader monad that either results in an error or an optional city
     */
    Reader<AppScopedLocator, Either<CriticalRepositoryNotFoundByLocatorError, Optional<FullCity>>> findCityById(
        long id);
}