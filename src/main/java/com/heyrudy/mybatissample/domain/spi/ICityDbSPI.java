package com.heyrudy.mybatissample.domain.spi;

import com.heyrudy.mybatissample.domain.model.city.ICity;
import com.heyrudy.mybatissample.domain.model.error.CriticalRepositoryNotFoundByLocatorError;
import com.heyrudy.mybatissample.domain.spi.config.AppScopedServiceLocator;
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
    Reader<AppScopedServiceLocator, Either<CriticalRepositoryNotFoundByLocatorError, ICity>> save(
        ICity fullCity);

    /**
     * Finds all cities.
     *
     * @return A Reader monad that either results in an error or a list of cities
     */
    Reader<AppScopedServiceLocator, Either<CriticalRepositoryNotFoundByLocatorError, List<ICity>>> findCities();

    /**
     * Finds a city by its ID.
     *
     * @param id The ID of the city to find
     * @return A Reader monad that either results in an error or an optional city
     */
    Reader<AppScopedServiceLocator, Either<CriticalRepositoryNotFoundByLocatorError, Optional<ICity>>> findCityById(
        long id);
}