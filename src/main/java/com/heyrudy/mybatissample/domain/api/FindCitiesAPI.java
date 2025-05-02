package com.heyrudy.mybatissample.domain.api;

import com.heyrudy.mybatissample.domain.model.city.ICity;
import com.heyrudy.mybatissample.domain.model.error.MissingCityDbCriticalServiceError;
import com.heyrudy.mybatissample.domain.model.utils.Workflow;
import com.heyrudy.mybatissample.domain.spi.config.AppScopedDependencyLocator;
import com.heyrudy.mybatissample.domain.spi.config.CityDbSPIKey;
import java.util.List;
import java.util.function.Function;

public final class FindCitiesAPI {

    public static final FindCitiesAPI INSTANCE = new FindCitiesAPI();

    private FindCitiesAPI() {
        super();
    }

    public Workflow<AppScopedDependencyLocator, MissingCityDbCriticalServiceError, List<ICity>> execute() {
        return appScopedDependencyLocator ->
            appScopedDependencyLocator.getDependency(CityDbSPIKey.INSTANCE)
                .bimap(
                    missingCriticalDependencyError ->
                        new MissingCityDbCriticalServiceError(
                            missingCriticalDependencyError.getMessage()),
                    iCityDbSPI ->
                        iCityDbSPI.findCities()
                            .apply(appScopedDependencyLocator)
                            .bimap(
                                criticalRepositoryNotFoundByLocatorError ->
                                    new MissingCityDbCriticalServiceError(
                                        criticalRepositoryNotFoundByLocatorError.getMessage()),
                                Function.identity()
                            )
                ).flatMap(Function.identity());
    }
}
