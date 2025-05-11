package com.heyrudy.mybatissample.domain.api;

import com.heyrudy.mybatissample.context.AppScopedDependencyLocator;
import com.heyrudy.mybatissample.context.MockedCityRepositoryKey;
import com.heyrudy.mybatissample.domain.model.city.ICity;
import com.heyrudy.mybatissample.domain.model.error.CityNotSavedByRepositoryError;
import com.heyrudy.mybatissample.domain.model.error.CityNotSavedError;
import com.heyrudy.mybatissample.domain.model.error.CriticalRepositoryNotFoundByDependencyLocatorError;
import com.heyrudy.mybatissample.domain.model.error.MissingCriticalDependencyError;
import com.heyrudy.mybatissample.domain.spi.ICityRepository;
import cyclops.control.Reader;
import io.vavr.control.Either;
import java.util.function.Function;

public enum CreateCityAPI {
    INSTANCE;

    private static final MockedCityRepositoryKey MOCKED_CITY_REPOSITORY_KEY = MockedCityRepositoryKey.INSTANCE;
    // Define error mapping functions
    private static final Function<MissingCriticalDependencyError, CriticalRepositoryNotFoundByDependencyLocatorError> MAP_DEPENDENCY_ERROR =
        missingCriticalDependencyError ->
            new CriticalRepositoryNotFoundByDependencyLocatorError(
                missingCriticalDependencyError.getMessage());
    private static final Function<Either<? extends MissingCriticalDependencyError, ICityRepository>, Either<CriticalRepositoryNotFoundByDependencyLocatorError, ICityRepository>> EITHER_TO_MAP_DEPENDENCY_ERROR = iCityRepositoryEither ->
        iCityRepositoryEither.mapLeft(MAP_DEPENDENCY_ERROR);
    private static final Function<CityNotSavedByRepositoryError, CityNotSavedError> MAP_SAVE_ERROR =
        cityNotSavedByRepositoryError ->
            new CityNotSavedError(
                cityNotSavedByRepositoryError.getMessage());
    private static final Function<Either<CityNotSavedByRepositoryError, ICity>, Either<CityNotSavedError, ICity>> EITHER_TO_MAP_SAVE_ERROR =
        either -> either.mapLeft(MAP_SAVE_ERROR);
    // A reader that always returns a specific error value
    private static final Function<CriticalRepositoryNotFoundByDependencyLocatorError, Reader<AppScopedDependencyLocator, Either<CityNotSavedError, ICity>>> CONSTANT_CITY_NOT_SAVED_ERROR_READER =
        criticalRepositoryNotFoundByDependencyLocatorError ->
            __ -> Either.left(new CityNotSavedError(
                criticalRepositoryNotFoundByDependencyLocatorError.getMessage()));

    public Reader<AppScopedDependencyLocator, Either<CityNotSavedError, ICity>> execute(
        final ICity iCity) {
        Function<ICityRepository, Reader<AppScopedDependencyLocator, Either<CityNotSavedError, ICity>>>
            saveWithRepository =
            iCityRepository -> iCityRepository.save(iCity).map(EITHER_TO_MAP_SAVE_ERROR);
        // Compose operations with flatMap to explicitly avoid apply
        return MOCKED_CITY_REPOSITORY_KEY.describeDependencyContext()
            .map(EITHER_TO_MAP_DEPENDENCY_ERROR)
            .flatMap(criticalRepositoryNotFoundByDependencyLocatorErrorICityRepositoryEither ->
                criticalRepositoryNotFoundByDependencyLocatorErrorICityRepositoryEither.fold(
                    CONSTANT_CITY_NOT_SAVED_ERROR_READER, saveWithRepository));
    }
}
