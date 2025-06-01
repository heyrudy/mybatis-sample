package com.heyrudy.mybatissample.gateway.db.repository;

import com.heyrudy.mybatissample.context.AppScopedDependencyLocator;
import com.heyrudy.mybatissample.domain.model.city.ICity;
import com.heyrudy.mybatissample.domain.error.DomainRepositoryError;
import com.heyrudy.mybatissample.domain.error.MissingCriticalDependencyError;
import cyclops.control.Reader;
import io.vavr.control.Either;
import io.vavr.control.Option;
import java.util.List;

public interface ICityRepository {

    /**
     * Saves a city.
     *
     * @param iCity The city to save
     * @return A Reader monad that either results in an error or the saved city
     */
    Reader<AppScopedDependencyLocator, Either<DomainRepositoryError, ICity>> save(ICity iCity);

    /**
     * Finds all cities.
     *
     * @return A Reader monad that either results in an error or a list of cities
     */
    Reader<AppScopedDependencyLocator, Either<MissingCriticalDependencyError, List<ICity>>> findAll();

    /**
     * Finds a city by its ID.
     *
     * @param id The ID of the city to find
     * @return A Reader monad that either results in an error or an optional city
     */
    Reader<AppScopedDependencyLocator, Either<DomainRepositoryError, Option<ICity>>> findById(
        long id);
}