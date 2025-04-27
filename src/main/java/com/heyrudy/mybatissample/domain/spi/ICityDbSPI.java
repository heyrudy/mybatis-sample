package com.heyrudy.mybatissample.domain.spi;

import com.heyrudy.mybatissample.domain.model.city.ICity;
import com.heyrudy.mybatissample.domain.model.error.CriticalRepositoryNotFoundByServiceLocatorError;
import com.heyrudy.mybatissample.domain.model.utils.Workflow;
import com.heyrudy.mybatissample.domain.spi.config.AppScopedServiceLocator;
import java.util.List;
import java.util.Optional;

public interface ICityDbSPI {

    /**
     * Saves a city.
     *
     * @param iCity The city to save
     * @return A Reader monad as a Workflow that either results in an error or the saved city
     */
    Workflow<AppScopedServiceLocator, CriticalRepositoryNotFoundByServiceLocatorError, ICity> save(
        ICity iCity);

    /**
     * Finds all cities.
     *
     * @return A Reader monad as a Workflow that either results in an error or a list of cities
     */
    Workflow<AppScopedServiceLocator, CriticalRepositoryNotFoundByServiceLocatorError, List<ICity>> findCities();

    /**
     * Finds a city by its ID.
     *
     * @param id The ID of the city to find
     * @return A Reader monad as a Workflow that either results in an error or an optional city
     */
    Workflow<AppScopedServiceLocator, CriticalRepositoryNotFoundByServiceLocatorError, Optional<ICity>> findCityById(
        long id);
}