package com.heyrudy.mybatissample.domain.api;

import com.heyrudy.mybatissample.context.AppScopedDependencyLocator;
import com.heyrudy.mybatissample.context.MockedCityRepositoryKey;
import com.heyrudy.mybatissample.domain.model.city.ICity;
import com.heyrudy.mybatissample.domain.model.error.CriticalDSLContextNotFoundByDependencyLocatorError;
import com.heyrudy.mybatissample.domain.model.error.CriticalRepositoryNotFoundByDependencyLocatorError;
import com.heyrudy.mybatissample.domain.model.error.MissingCriticalDependencyError;
import com.heyrudy.mybatissample.domain.spi.ICityRepository;
import cyclops.control.Reader;
import io.vavr.control.Either;
import java.util.List;
import java.util.function.Function;

public enum FindCitiesAPI {
    INSTANCE;

    private static final MockedCityRepositoryKey MOCKED_CITY_REPOSITORY_KEY = MockedCityRepositoryKey.INSTANCE;
    // A reader that always returns a specific error value
    private static final Function<MissingCriticalDependencyError, Reader<AppScopedDependencyLocator, Either<CriticalRepositoryNotFoundByDependencyLocatorError, List<ICity>>>> DEPENDENCY_NEVER_FOUND_PATH =
        missingCriticalDependencyError ->
            __ -> Either.left(new CriticalRepositoryNotFoundByDependencyLocatorError(
                missingCriticalDependencyError.getMessage()));
    private static final Function<CriticalDSLContextNotFoundByDependencyLocatorError, Either<CriticalRepositoryNotFoundByDependencyLocatorError, List<ICity>>> CRITICAL_DSL_CONTEXT_NOT_FOUND_BY_DEPENDENCY_LOCATOR_PATH =
        criticalDSLContextNotFoundByDependencyLocatorError ->
            Either.left(
                new CriticalRepositoryNotFoundByDependencyLocatorError(
                    criticalDSLContextNotFoundByDependencyLocatorError.getMessage()));
    private static final Function<Either<CriticalDSLContextNotFoundByDependencyLocatorError, List<ICity>>, Either<CriticalRepositoryNotFoundByDependencyLocatorError, List<ICity>>> FIND_CITIES_PATH =
        criticalDSLContextNotFoundByDependencyLocatorErrorListEither ->
            criticalDSLContextNotFoundByDependencyLocatorErrorListEither.fold(
                CRITICAL_DSL_CONTEXT_NOT_FOUND_BY_DEPENDENCY_LOCATOR_PATH, Either::right);
    private static final Function<ICityRepository, Reader<AppScopedDependencyLocator, Either<CriticalRepositoryNotFoundByDependencyLocatorError, List<ICity>>>> FIND_CITIES_BY_REPOSITORY_PATH =
        iCityRepository -> iCityRepository.findAll().map(FIND_CITIES_PATH);

    public Reader<AppScopedDependencyLocator, Either<CriticalRepositoryNotFoundByDependencyLocatorError, List<ICity>>> execute() {
        // Compose operations with flatMap to explicitly avoid apply
        return MOCKED_CITY_REPOSITORY_KEY.describeDependencyContext()
            .flatMap(iCityRepositoryEither ->
                iCityRepositoryEither.fold(
                    DEPENDENCY_NEVER_FOUND_PATH, FIND_CITIES_BY_REPOSITORY_PATH));
    }
}
