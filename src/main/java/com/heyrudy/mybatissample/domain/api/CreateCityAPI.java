package com.heyrudy.mybatissample.domain.api;

import com.heyrudy.mybatissample.domain.model.city.ICity;
import com.heyrudy.mybatissample.domain.model.error.CriticalRepositoryNotFoundByDependencyLocatorError;
import com.heyrudy.mybatissample.context.AppScopedDependencyLocator;
import com.heyrudy.mybatissample.context.CityRepositoryKey;
import cyclops.control.Reader;
import io.vavr.control.Either;

public final class CreateCityAPI {

    public static final CreateCityAPI INSTANCE = new CreateCityAPI();

    private CreateCityAPI() {
        super();
    }

    public Reader<AppScopedDependencyLocator, Either<CriticalRepositoryNotFoundByDependencyLocatorError, ICity>> execute(
        final ICity iCity) {
        return appScopedDependencyLocator ->
            CityRepositoryKey.INSTANCE.describeDependencyContext()
                .apply(appScopedDependencyLocator)
                .mapLeft(missingCriticalDependencyError ->
                    new CriticalRepositoryNotFoundByDependencyLocatorError(
                        missingCriticalDependencyError.getMessage()))
                .flatMap(iCityRepository ->
                    iCityRepository.save(iCity)
                        .apply(appScopedDependencyLocator)
                        .mapLeft(cityNotSavedByRepositoryError ->
                            new CriticalRepositoryNotFoundByDependencyLocatorError(
                                cityNotSavedByRepositoryError.getMessage())));
    }
}
