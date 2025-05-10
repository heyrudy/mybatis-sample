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

    // Define error mapping functions
    public static final Function<MissingCriticalDependencyError, CriticalRepositoryNotFoundByDependencyLocatorError> MAP_DEPENDENCY_ERROR = missingCriticalDependencyError ->
        new CriticalRepositoryNotFoundByDependencyLocatorError(
            missingCriticalDependencyError.getMessage());
    public static final Function<CriticalDSLContextNotFoundByDependencyLocatorError, CriticalRepositoryNotFoundByDependencyLocatorError> MAP_DSL_ERROR = criticalDSLContextNotFoundByDependencyLocatorError ->
        new CriticalRepositoryNotFoundByDependencyLocatorError(
            criticalDSLContextNotFoundByDependencyLocatorError.getMessage());
    // A reader that always returns a specific error value
    public static final Function<CriticalRepositoryNotFoundByDependencyLocatorError, Reader<AppScopedDependencyLocator, Either<CriticalRepositoryNotFoundByDependencyLocatorError, List<ICity>>>> CONSTANT_REPOSITORY_NOT_FOUND_BY_DEPENDENCY_LOCATOR_ERROR_READER = criticalRepositoryNotFoundByDependencyLocatorError ->
        __ -> Either.left(criticalRepositoryNotFoundByDependencyLocatorError);
    public static final Function<ICityRepository, Reader<AppScopedDependencyLocator, Either<CriticalRepositoryNotFoundByDependencyLocatorError, List<ICity>>>> FIND_ALL_WITH_REPOSITORY = iCityRepository ->
        iCityRepository.findAll()
            .map(criticalDSLContextNotFoundByDependencyLocatorErrorListEither ->
                criticalDSLContextNotFoundByDependencyLocatorErrorListEither.mapLeft(
                    MAP_DSL_ERROR));

    public Reader<AppScopedDependencyLocator, Either<CriticalRepositoryNotFoundByDependencyLocatorError, List<ICity>>> execute() {
        // Compose operations with flatMap to explicitly avoid apply
        return MockedCityRepositoryKey.INSTANCE.describeDependencyContext()
            .map(iCityRepositoryEither ->
                iCityRepositoryEither.mapLeft(MAP_DEPENDENCY_ERROR))
            .flatMap(criticalRepositoryNotFoundByDependencyLocatorErrorICityRepositoryEither ->
                criticalRepositoryNotFoundByDependencyLocatorErrorICityRepositoryEither.fold(
                    CONSTANT_REPOSITORY_NOT_FOUND_BY_DEPENDENCY_LOCATOR_ERROR_READER,
                    FIND_ALL_WITH_REPOSITORY));
    }
}
