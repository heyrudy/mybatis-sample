package com.heyrudy.mybatissample.domain.api;

import com.heyrudy.mybatissample.domain.model.city.ICity;
import com.heyrudy.mybatissample.domain.model.error.MissingCityDbRepositoryCriticalServiceError;
import com.heyrudy.mybatissample.domain.spi.config.AppScopedServiceLocator;
import com.heyrudy.mybatissample.domain.spi.config.ServiceKey.CityDbSPIKey;
import cyclops.control.Reader;
import io.vavr.control.Either;
import java.util.function.Function;

public final class CreateCityAPI {

    public static final CreateCityAPI INSTANCE = new CreateCityAPI();

    private CreateCityAPI() {
        super();
    }

    public Reader<AppScopedServiceLocator, Either<MissingCityDbRepositoryCriticalServiceError, ICity>> execute(
        final ICity city) {
        return locator ->
            locator.getDbCriticalService(CityDbSPIKey.INSTANCE)
                .bimap(
                    dbCriticalServiceNotFoundByLocatorError ->
                        new MissingCityDbRepositoryCriticalServiceError(
                            dbCriticalServiceNotFoundByLocatorError.getMessage()),
                    iCityDbSPI ->
                        iCityDbSPI.save(city)
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
