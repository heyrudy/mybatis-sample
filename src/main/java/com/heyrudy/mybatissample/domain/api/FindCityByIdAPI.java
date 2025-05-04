package com.heyrudy.mybatissample.domain.api;

import com.heyrudy.mybatissample.domain.model.city.ICity;
import com.heyrudy.mybatissample.domain.model.common.CityCriteriaDetails;
import com.heyrudy.mybatissample.domain.model.error.CityNotFoundError;
import com.heyrudy.mybatissample.domain.model.error.CityNotFoundError.ErrorMessage;
import com.heyrudy.mybatissample.domain.model.utils.Workflow;
import com.heyrudy.mybatissample.domain.spi.config.AppScopedDependencyLocator;
import com.heyrudy.mybatissample.domain.spi.config.CityRepositoryKey;
import io.vavr.control.Option;

public final class FindCityByIdAPI {

    public static final FindCityByIdAPI INSTANCE = new FindCityByIdAPI();

    private FindCityByIdAPI() {
        super();
    }

    /**
     * Finds a city by its ID.
     *
     * @param cityCriteriaDetails The city details to find
     * @return A Reader monad as a Workflow that either results in an error or an optional city
     */
    public Workflow<AppScopedDependencyLocator, CityNotFoundError, ICity> execute(
        final CityCriteriaDetails cityCriteriaDetails) {
        return appScopedDependencyLocator ->
            appScopedDependencyLocator.getDependency(CityRepositoryKey.INSTANCE)
                .mapLeft(missingCriticalDependencyError ->
                    new CityNotFoundError(
                        missingCriticalDependencyError.getMessage()))
                .flatMap(iCityRepository ->
                    iCityRepository.findById(cityCriteriaDetails.cityId())
                        .apply(appScopedDependencyLocator)
                        .mapLeft(cityNotFoundByRepositoryError ->
                            new CityNotFoundError(
                                cityNotFoundByRepositoryError.getMessage()))
                        .flatMap(optionalCity ->
                            Option.ofOptional(optionalCity)
                                .toEither(new CityNotFoundError(
                                    ErrorMessage.CITY_NOT_FOUND_ERROR_MESSAGE.formatted(
                                        cityCriteriaDetails.cityId())))));
    }
}
