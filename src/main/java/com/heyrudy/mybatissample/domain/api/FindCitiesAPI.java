package com.heyrudy.mybatissample.domain.api;

import com.heyrudy.mybatissample.domain.model.city.ICity;
import com.heyrudy.mybatissample.domain.model.error.MissingCityDbRepositoryCriticalServiceError;
import com.heyrudy.mybatissample.domain.spi.config.AppScopedServiceLocator;
import com.heyrudy.mybatissample.domain.spi.config.ServiceKey.CityDbSPIKey;
import cyclops.control.Reader;
import io.vavr.control.Either;
import java.util.List;
import java.util.function.Function;

public final class FindCitiesAPI {

    public static final FindCitiesAPI INSTANCE = new FindCitiesAPI();

    private FindCitiesAPI() {
        super();
    }

    public Reader<AppScopedServiceLocator, Either<MissingCityDbRepositoryCriticalServiceError, List<ICity>>> execute() {
        return locator ->
            locator.getDbCriticalService(CityDbSPIKey.INSTANCE)
                .bimap(
                    dbCriticalServiceNotFoundByLocatorError ->
                        new MissingCityDbRepositoryCriticalServiceError(
                            dbCriticalServiceNotFoundByLocatorError.getMessage()),
                    iCityDbSPI ->
                        iCityDbSPI.findCities()
                            .apply(locator)
                            .bimap(
                                criticalRepositoryNotFoundByLocatorError ->
                                    new MissingCityDbRepositoryCriticalServiceError(
                                        criticalRepositoryNotFoundByLocatorError.getMessage()),
                                Function.identity()
                            )
                ).flatMap(Function.identity());
    }
}
