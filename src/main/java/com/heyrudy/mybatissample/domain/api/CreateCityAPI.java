package com.heyrudy.mybatissample.domain.api;

import com.heyrudy.mybatissample.domain.model.city.ICity;
import com.heyrudy.mybatissample.domain.model.error.MissingCityDbCriticalServiceError;
import com.heyrudy.mybatissample.domain.model.utils.Workflow;
import com.heyrudy.mybatissample.domain.spi.config.AppScopedServiceLocator;
import com.heyrudy.mybatissample.domain.spi.config.CityDbSPIKey;
import java.util.function.Function;

public final class CreateCityAPI {

    public static final CreateCityAPI INSTANCE = new CreateCityAPI();

    private CreateCityAPI() {
        super();
    }

    public Workflow<AppScopedServiceLocator, MissingCityDbCriticalServiceError, ICity> execute(
        final ICity city) {
        return appScopedServiceLocator ->
            appScopedServiceLocator.getDbCriticalService(CityDbSPIKey.INSTANCE)
                .bimap(
                    dbCriticalServiceNotFoundByLocatorError ->
                        new MissingCityDbCriticalServiceError(
                            dbCriticalServiceNotFoundByLocatorError.getMessage()),
                    iCityDbSPI ->
                        iCityDbSPI.save(city)
                            .apply(appScopedServiceLocator)
                            .bimap(
                                criticalRepositoryNotFoundByLocatorError ->
                                    new MissingCityDbCriticalServiceError(
                                        criticalRepositoryNotFoundByLocatorError.getMessage()),
                                Function.identity()
                            )
                ).flatMap(Function.identity());
    }
}
