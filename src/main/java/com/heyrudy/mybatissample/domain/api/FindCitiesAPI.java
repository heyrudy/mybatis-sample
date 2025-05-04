package com.heyrudy.mybatissample.domain.api;

import com.heyrudy.mybatissample.domain.model.city.ICity;
import com.heyrudy.mybatissample.domain.model.error.CriticalRepositoryNotFoundByDependencyLocatorError;
import com.heyrudy.mybatissample.domain.model.utils.Workflow;
import com.heyrudy.mybatissample.domain.spi.config.AppScopedDependencyLocator;
import com.heyrudy.mybatissample.domain.spi.config.CityRepositoryKey;
import java.util.List;

public final class FindCitiesAPI {

    public static final FindCitiesAPI INSTANCE = new FindCitiesAPI();

    private FindCitiesAPI() {
        super();
    }

    /**
     * Finds all cities.
     *
     * @return A Reader monad as a Workflow that either results in an error or a list of cities
     */
    public Workflow<AppScopedDependencyLocator, CriticalRepositoryNotFoundByDependencyLocatorError, List<ICity>> execute() {
        return appScopedDependencyLocator ->
            appScopedDependencyLocator.getDependency(CityRepositoryKey.INSTANCE)
                .mapLeft(missingCriticalDependencyError ->
                    new CriticalRepositoryNotFoundByDependencyLocatorError(
                        missingCriticalDependencyError.getMessage()))
                .flatMap(iCityRepository ->
                    iCityRepository.findAll()
                        .apply(appScopedDependencyLocator)
                        .mapLeft(criticalDSLContextNotFoundByDependencyLocatorError ->
                            new CriticalRepositoryNotFoundByDependencyLocatorError(
                                criticalDSLContextNotFoundByDependencyLocatorError.getMessage())));
    }
}
