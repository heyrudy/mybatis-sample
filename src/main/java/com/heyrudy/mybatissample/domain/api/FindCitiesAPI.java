package com.heyrudy.mybatissample.domain.api;

import com.heyrudy.mybatissample.domain.model.city.ICity;
import com.heyrudy.mybatissample.domain.model.error.MissingCityDbCriticalServiceError;
import com.heyrudy.mybatissample.domain.model.utils.Workflow;
import com.heyrudy.mybatissample.domain.spi.config.AppScopedServiceLocator;
import com.heyrudy.mybatissample.domain.spi.config.CityDbSPIKey;
import java.util.List;
import java.util.function.Function;

public final class FindCitiesAPI {

    public static final FindCitiesAPI INSTANCE = new FindCitiesAPI();

    private FindCitiesAPI() {
        super();
    }

    public Workflow<AppScopedServiceLocator, MissingCityDbCriticalServiceError, List<ICity>> execute() {
        return appScopedServiceLocator ->
            appScopedServiceLocator.getDbCriticalService(CityDbSPIKey.INSTANCE)
                .bimap(
                    dbCriticalServiceNotFoundByServiceLocatorError ->
                        new MissingCityDbCriticalServiceError(
                            dbCriticalServiceNotFoundByServiceLocatorError.getMessage()),
                    iCityDbSPI ->
                        iCityDbSPI.findCities()
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
