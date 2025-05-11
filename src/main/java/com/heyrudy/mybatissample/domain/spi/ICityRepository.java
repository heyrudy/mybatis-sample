package com.heyrudy.mybatissample.domain.spi;

import com.heyrudy.mybatissample.context.AppScopedDependencyLocator;
import com.heyrudy.mybatissample.domain.model.city.ICity;
import com.heyrudy.mybatissample.domain.model.error.CityNotFoundByRepositoryError;
import com.heyrudy.mybatissample.domain.model.error.CityNotSavedByRepositoryError;
import com.heyrudy.mybatissample.domain.model.error.CriticalDSLContextNotFoundByDependencyLocatorError;
import cyclops.control.Reader;
import io.vavr.control.Either;
import io.vavr.control.Option;
import java.util.List;

public interface ICityRepository {

    /**
     * Saves a city.
     *
     * @param iCity The city to save
     * @return A Reader monad as a Workflow that either results in an error or the saved city
     */
    Reader<AppScopedDependencyLocator, Either<CityNotSavedByRepositoryError, ICity>> save(ICity iCity);

    /**
     * Finds all cities.
     *
     * @return A Reader monad as a Reader that either results in an error or a list of cities
     */
    Reader<AppScopedDependencyLocator, Either<CriticalDSLContextNotFoundByDependencyLocatorError, List<ICity>>> findAll();

    /**
     * Finds a city by its ID.
     *
     * @param id The ID of the city to find
     * @return A Reader monad as a Reader that either results in an error or an optional city
     */
    Reader<AppScopedDependencyLocator, Either<CityNotFoundByRepositoryError, Option<ICity>>> findById(
        long id);
}