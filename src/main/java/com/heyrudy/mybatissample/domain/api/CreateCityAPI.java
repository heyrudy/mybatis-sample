package com.heyrudy.mybatissample.domain.api;

import com.heyrudy.mybatissample.context.AppScopedDependencyLocator;
import com.heyrudy.mybatissample.context.MockedCityRepositoryKey;
import com.heyrudy.mybatissample.domain.model.city.ICity;
import com.heyrudy.mybatissample.domain.model.error.CityNotSavedByRepositoryError;
import com.heyrudy.mybatissample.domain.model.error.CityNotSavedError;
import com.heyrudy.mybatissample.domain.model.error.MissingCriticalDependencyError;
import cyclops.control.Reader;
import io.vavr.control.Either;
import java.util.function.Function;

public enum CreateCityAPI {
    INSTANCE;

    private static final MockedCityRepositoryKey MOCKED_CITY_REPOSITORY_KEY = MockedCityRepositoryKey.INSTANCE;
    // A reader that always returns a specific error value
    private static final Function<MissingCriticalDependencyError, Reader<AppScopedDependencyLocator, Either<CityNotSavedError, ICity>>> DEPENDENCY_NEVER_FOUND_PATH =
        missingCriticalDependencyError ->
            __ ->
                Either.left(new CityNotSavedError(missingCriticalDependencyError.getMessage()));
    private static final Function<CityNotSavedByRepositoryError, Either<CityNotSavedError, ICity>> CITY_NOT_SAVED_BY_REPOSITORY_PATH =
        cityNotSavedByRepositoryError ->
            Either.left(new CityNotSavedError(cityNotSavedByRepositoryError.getMessage()));
    private static final Function<Either<CityNotSavedByRepositoryError, ICity>, Either<CityNotSavedError, ICity>> SAVE_CITY_PATH =
        cityNotSavedByRepositoryErrorICityEither ->
            cityNotSavedByRepositoryErrorICityEither.fold(
                CITY_NOT_SAVED_BY_REPOSITORY_PATH, Either::right);

    public Reader<AppScopedDependencyLocator, Either<CityNotSavedError, ICity>> execute(
        final ICity iCity) {
        // Compose operations with flatMap to explicitly avoid apply
        return MOCKED_CITY_REPOSITORY_KEY.describeDependencyContext()
            .flatMap(iCityRepositoryEither ->
                iCityRepositoryEither.fold(
                    DEPENDENCY_NEVER_FOUND_PATH,
                    iCityRepository -> iCityRepository.save(iCity).map(SAVE_CITY_PATH)));
    }
}
