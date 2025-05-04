package com.heyrudy.mybatissample.domain.api;

import com.heyrudy.mybatissample.domain.model.city.ICity;
import com.heyrudy.mybatissample.domain.model.common.CityCriteriaDetails;
import com.heyrudy.mybatissample.domain.model.error.CityNotFoundError;
import com.heyrudy.mybatissample.domain.model.error.CityNotFoundError.ErrorMessage;
import com.heyrudy.mybatissample.domain.model.error.MissingCityDbCriticalServiceError;
import com.heyrudy.mybatissample.domain.model.error.MissingCityError;
import com.heyrudy.mybatissample.domain.model.utils.Workflow;
import com.heyrudy.mybatissample.domain.spi.config.AppScopedDependencyLocator;
import com.heyrudy.mybatissample.domain.spi.config.CityDbSPIKey;
import io.vavr.control.Option;

public final class FindCityByIdAPI {

    public static final FindCityByIdAPI INSTANCE = new FindCityByIdAPI();

    private FindCityByIdAPI() {
        super();
    }

    public Workflow<AppScopedDependencyLocator, MissingCityError, ICity> execute(
        final CityCriteriaDetails cityCriteriaDetails) {
        return appScopedDependencyLocator ->
            appScopedDependencyLocator.getDependency(CityDbSPIKey.INSTANCE)
                .<MissingCityError>mapLeft(missingCriticalDependencyError ->
                    new MissingCityDbCriticalServiceError(
                        missingCriticalDependencyError.getMessage()))
                .flatMap(iCityDbSPI ->
                    iCityDbSPI.findCityById(cityCriteriaDetails.cityId())
                        .apply(appScopedDependencyLocator)
                        .<MissingCityError>mapLeft(criticalRepositoryNotFoundByLocatorError ->
                            new MissingCityDbCriticalServiceError(
                                criticalRepositoryNotFoundByLocatorError.getMessage())
                        )
                        .flatMap(optionalCity ->
                            Option.ofOptional(optionalCity)
                                .toEither(new CityNotFoundError(
                                    ErrorMessage.CITY_NOT_FOUND_ERROR_MESSAGE.formatted(
                                        cityCriteriaDetails.cityId())))));
    }
}
