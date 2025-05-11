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
    // Define error mapping functions
    private static final Function<MissingCriticalDependencyError, CriticalRepositoryNotFoundByDependencyLocatorError> MAP_DEPENDENCY_ERROR =
        missingCriticalDependencyError ->
            new CriticalRepositoryNotFoundByDependencyLocatorError(
                missingCriticalDependencyError.getMessage());
    private static final Function<Either<? extends MissingCriticalDependencyError, ICityRepository>, Either<CriticalRepositoryNotFoundByDependencyLocatorError, ICityRepository>> EITHER_TO_MAP_DEPENDENCY_ERROR =
        iCityRepositoryEither ->
            iCityRepositoryEither.mapLeft(MAP_DEPENDENCY_ERROR);
    private static final Function<CriticalDSLContextNotFoundByDependencyLocatorError, CriticalRepositoryNotFoundByDependencyLocatorError> MAP_DSL_ERROR =
        criticalDSLContextNotFoundByDependencyLocatorError ->
            new CriticalRepositoryNotFoundByDependencyLocatorError(
                criticalDSLContextNotFoundByDependencyLocatorError.getMessage());
    private static final Function<Either<CriticalDSLContextNotFoundByDependencyLocatorError, List<ICity>>, Either<CriticalRepositoryNotFoundByDependencyLocatorError, List<ICity>>> EITHER_TO_MAP_DSL_ERROR =
        criticalDSLContextNotFoundByDependencyLocatorErrorListEither ->
            criticalDSLContextNotFoundByDependencyLocatorErrorListEither.mapLeft(MAP_DSL_ERROR);
    // A reader that always returns a specific error value
    private static final Function<CriticalRepositoryNotFoundByDependencyLocatorError, Reader<AppScopedDependencyLocator, Either<CriticalRepositoryNotFoundByDependencyLocatorError, List<ICity>>>> CONSTANT_REPOSITORY_NOT_FOUND_BY_DEPENDENCY_LOCATOR_ERROR_READER =
        criticalRepositoryNotFoundByDependencyLocatorError ->
            __ -> Either.left(criticalRepositoryNotFoundByDependencyLocatorError);
    private static final Function<ICityRepository, Reader<AppScopedDependencyLocator, Either<CriticalRepositoryNotFoundByDependencyLocatorError, List<ICity>>>> FIND_ALL_WITH_REPOSITORY =
        iCityRepository -> iCityRepository.findAll().map(EITHER_TO_MAP_DSL_ERROR);
    private static final Function<Either<CriticalRepositoryNotFoundByDependencyLocatorError, ICityRepository>, Reader<AppScopedDependencyLocator, Either<CriticalRepositoryNotFoundByDependencyLocatorError, List<ICity>>>> EITHER_TO_FIND_ALL_WITH_REPOSITORY =
        criticalRepositoryNotFoundByDependencyLocatorErrorICityRepositoryEither ->
            criticalRepositoryNotFoundByDependencyLocatorErrorICityRepositoryEither.fold(
                CONSTANT_REPOSITORY_NOT_FOUND_BY_DEPENDENCY_LOCATOR_ERROR_READER,
                FIND_ALL_WITH_REPOSITORY);

    public Reader<AppScopedDependencyLocator, Either<CriticalRepositoryNotFoundByDependencyLocatorError, List<ICity>>> execute() {
        // Compose operations with flatMap to explicitly avoid apply
        return MOCKED_CITY_REPOSITORY_KEY.describeDependencyContext()
            .map(EITHER_TO_MAP_DEPENDENCY_ERROR)
            .flatMap(EITHER_TO_FIND_ALL_WITH_REPOSITORY);
    }
}
