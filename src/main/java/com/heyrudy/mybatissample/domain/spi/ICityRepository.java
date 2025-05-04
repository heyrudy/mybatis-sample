package com.heyrudy.mybatissample.domain.spi;

import com.heyrudy.mybatissample.domain.model.city.ICity;
import com.heyrudy.mybatissample.domain.model.error.CityNotFoundByRepositoryError;
import com.heyrudy.mybatissample.domain.model.error.CityNotSavedByRepositoryError;
import com.heyrudy.mybatissample.domain.model.error.CriticalDSLContextNotFoundByDependencyLocatorError;
import com.heyrudy.mybatissample.domain.model.utils.Workflow;
import com.heyrudy.mybatissample.domain.spi.config.AppScopedDependencyLocator;
import java.util.List;
import java.util.Optional;

public interface ICityRepository {

    /**
     * Saves a city.
     *
     * @param iCity The city to save
     * @return A Reader monad as a Workflow that either results in an error or the saved city
     */
    Workflow<AppScopedDependencyLocator, CityNotSavedByRepositoryError, ICity> save(ICity iCity);

    /**
     * Finds all cities.
     *
     * @return A Reader monad as a Workflow that either results in an error or a list of cities
     */
    Workflow<AppScopedDependencyLocator, CriticalDSLContextNotFoundByDependencyLocatorError, List<ICity>> findAll();

    /**
     * Finds a city by its ID.
     *
     * @param id The ID of the city to find
     * @return A Reader monad as a Workflow that either results in an error or an optional city
     */
    Workflow<AppScopedDependencyLocator, CityNotFoundByRepositoryError, Optional<ICity>> findById(
        long id);
}