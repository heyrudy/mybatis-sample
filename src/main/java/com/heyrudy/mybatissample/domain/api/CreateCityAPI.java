package com.heyrudy.mybatissample.domain.api;

import com.heyrudy.mybatissample.context.AppScopedDependencyLocator;
import com.heyrudy.mybatissample.context.MockedCityRepositoryKey;
import com.heyrudy.mybatissample.domain.model.city.ICity;
import com.heyrudy.mybatissample.domain.model.error.CityNotSavedByRepositoryError;
import com.heyrudy.mybatissample.domain.model.error.CriticalRepositoryNotFoundByDependencyLocatorError;
import com.heyrudy.mybatissample.domain.model.error.MissingCriticalDependencyError;
import com.heyrudy.mybatissample.domain.spi.ICityRepository;
import cyclops.control.Reader;
import io.vavr.control.Either;
import java.util.function.Function;

public enum CreateCityAPI {
    INSTANCE;

    // Define error mapping functions
    public static final Function<MissingCriticalDependencyError, CriticalRepositoryNotFoundByDependencyLocatorError> MAP_DEPENDENCY_ERROR = missingCriticalDependencyError ->
        new CriticalRepositoryNotFoundByDependencyLocatorError(
            missingCriticalDependencyError.getMessage());
    public static final Function<CityNotSavedByRepositoryError, CriticalRepositoryNotFoundByDependencyLocatorError> MAP_SAVE_ERROR = cityNotSavedByRepositoryError ->
        new CriticalRepositoryNotFoundByDependencyLocatorError(
            cityNotSavedByRepositoryError.getMessage());
    // A reader that always returns a specific error value
    public static final Function<CriticalRepositoryNotFoundByDependencyLocatorError, Reader<AppScopedDependencyLocator, Either<CriticalRepositoryNotFoundByDependencyLocatorError, ICity>>> CONSTANT_REPOSITORY_NOT_FOUND_BY_DEPENDENCY_LOCATOR_ERROR_READER = criticalRepositoryNotFoundByDependencyLocatorError ->
        __ -> Either.left(criticalRepositoryNotFoundByDependencyLocatorError);

    public Reader<AppScopedDependencyLocator, Either<CriticalRepositoryNotFoundByDependencyLocatorError, ICity>> execute(
        final ICity iCity) {
        Function<ICityRepository, Reader<AppScopedDependencyLocator, Either<CriticalRepositoryNotFoundByDependencyLocatorError, ICity>>>
            saveWithRepository = iCityRepository ->
            iCityRepository.save(iCity).map(either -> either.mapLeft(MAP_SAVE_ERROR));
        // Compose operations with flatMap to explicitly avoid apply
        return MockedCityRepositoryKey.INSTANCE.describeDependencyContext()
            .map(iCityRepositoryEither ->
                iCityRepositoryEither.mapLeft(MAP_DEPENDENCY_ERROR))
            .flatMap(criticalRepositoryNotFoundByDependencyLocatorErrorICityRepositoryEither ->
                criticalRepositoryNotFoundByDependencyLocatorErrorICityRepositoryEither.fold(
                    CONSTANT_REPOSITORY_NOT_FOUND_BY_DEPENDENCY_LOCATOR_ERROR_READER,
                    saveWithRepository));
    }
}
