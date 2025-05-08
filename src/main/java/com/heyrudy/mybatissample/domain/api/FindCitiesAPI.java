package com.heyrudy.mybatissample.domain.api;

import com.heyrudy.mybatissample.domain.model.city.ICity;
import com.heyrudy.mybatissample.domain.model.error.CriticalRepositoryNotFoundByDependencyLocatorError;
import com.heyrudy.mybatissample.context.AppScopedDependencyLocator;
import com.heyrudy.mybatissample.context.CityRepositoryKey;
import cyclops.control.Reader;
import io.vavr.control.Either;
import java.util.List;

public final class FindCitiesAPI {

    public static final FindCitiesAPI INSTANCE = new FindCitiesAPI();

    private FindCitiesAPI() {
        super();
    }

    public Reader<AppScopedDependencyLocator, Either<CriticalRepositoryNotFoundByDependencyLocatorError, List<ICity>>> execute() {
        return appScopedDependencyLocator ->
            CityRepositoryKey.INSTANCE.describeDependencyContext()
                .apply(appScopedDependencyLocator)
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
