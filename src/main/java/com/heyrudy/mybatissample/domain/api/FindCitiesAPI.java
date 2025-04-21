package com.heyrudy.mybatissample.domain.api;

import com.heyrudy.mybatissample.domain.model.city.ICity;
import com.heyrudy.mybatissample.domain.model.error.MissingCityDbCriticalServiceError;
import com.heyrudy.mybatissample.domain.spi.config.AppScopedServiceLocator;
import com.heyrudy.mybatissample.domain.spi.config.ServiceKey.CityDbSPIKey;
import com.heyrudy.mybatissample.domain.spi.config.Workflow;
import java.util.List;
import java.util.function.Function;

public final class FindCitiesAPI {

    public static final FindCitiesAPI INSTANCE = new FindCitiesAPI();

    private FindCitiesAPI() {
        super();
    }

    public Workflow<AppScopedServiceLocator, MissingCityDbCriticalServiceError, List<ICity>> execute() {
        return locator ->
            locator.getDbCriticalService(CityDbSPIKey.INSTANCE)
                .bimap(
                    dbCriticalServiceNotFoundByLocatorError ->
                        new MissingCityDbCriticalServiceError(
                            dbCriticalServiceNotFoundByLocatorError.getMessage()),
                    iCityDbSPI ->
                        iCityDbSPI.findCities()
                            .apply(locator)
                            .bimap(
                                criticalRepositoryNotFoundByLocatorError ->
                                    new MissingCityDbCriticalServiceError(
                                        criticalRepositoryNotFoundByLocatorError.getMessage()),
                                Function.identity()
                            )
                ).flatMap(Function.identity());
    }
}
