package com.heyrudy.mybatissample.domain.api;

import com.heyrudy.mybatissample.domain.model.city.ICity;
import com.heyrudy.mybatissample.domain.model.error.MissingCityDbCriticalServiceError;
import com.heyrudy.mybatissample.domain.model.utils.Workflow;
import com.heyrudy.mybatissample.domain.spi.config.AppScopedDependencyLocator;
import com.heyrudy.mybatissample.domain.spi.config.CityDbSPIKey;
import java.util.function.Function;

public final class CreateCityAPI {

    public static final CreateCityAPI INSTANCE = new CreateCityAPI();

    private CreateCityAPI() {
        super();
    }

    public Workflow<AppScopedDependencyLocator, MissingCityDbCriticalServiceError, ICity> execute(
        final ICity city) {
        return appScopedDependencyLocator ->
            appScopedDependencyLocator.getDependency(CityDbSPIKey.INSTANCE)
                .bimap(
                    missingCriticalDependencyError ->
                        new MissingCityDbCriticalServiceError(
                            missingCriticalDependencyError.getMessage()),
                    iCityDbSPI ->
                        iCityDbSPI.save(city)
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
