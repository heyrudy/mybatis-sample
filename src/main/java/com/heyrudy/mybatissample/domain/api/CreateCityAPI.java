package com.heyrudy.mybatissample.domain.api;

import com.heyrudy.mybatissample.domain.model.city.ICity;
import com.heyrudy.mybatissample.domain.model.error.CriticalRepositoryNotFoundByDependencyLocatorError;
import com.heyrudy.mybatissample.domain.model.utils.Workflow;
import com.heyrudy.mybatissample.domain.spi.config.AppScopedDependencyLocator;
import com.heyrudy.mybatissample.domain.spi.config.CityRepositoryKey;

public final class CreateCityAPI {

    public static final CreateCityAPI INSTANCE = new CreateCityAPI();

    private CreateCityAPI() {
        super();
    }

    /**
     * Saves a city.
     *
     * @param city The city to save
     * @return A Reader monad as a Workflow that either results in an error or the saved city
     */
    public Workflow<AppScopedDependencyLocator, CriticalRepositoryNotFoundByDependencyLocatorError, ICity> execute(
        final ICity city) {
        return appScopedDependencyLocator ->
            appScopedDependencyLocator.getDependency(CityRepositoryKey.INSTANCE)
                .mapLeft(missingCriticalDependencyError ->
                    new CriticalRepositoryNotFoundByDependencyLocatorError(
                        missingCriticalDependencyError.getMessage()))
                .flatMap(iCityDbSPI ->
                    iCityDbSPI.save(city)
                        .apply(appScopedDependencyLocator)
                        .mapLeft(cityNotSavedByRepositoryError ->
                            new CriticalRepositoryNotFoundByDependencyLocatorError(
                                cityNotSavedByRepositoryError.getMessage())));
    }
}
