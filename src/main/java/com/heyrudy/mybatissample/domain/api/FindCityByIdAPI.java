package com.heyrudy.mybatissample.domain.api;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Patterns.$None;
import static io.vavr.Patterns.$Some;

import com.heyrudy.mybatissample.context.AppScopedDependencyLocator;
import com.heyrudy.mybatissample.context.MockedCityRepositoryKey;
import com.heyrudy.mybatissample.domain.model.city.ICity;
import com.heyrudy.mybatissample.domain.model.common.CityCriteriaDetails;
import com.heyrudy.mybatissample.domain.model.error.CityNotFoundByRepositoryError;
import com.heyrudy.mybatissample.domain.model.error.CityNotFoundError;
import com.heyrudy.mybatissample.domain.model.error.CityNotFoundError.ErrorMessage;
import com.heyrudy.mybatissample.domain.model.error.MissingCriticalDependencyError;
import cyclops.control.Reader;
import io.vavr.control.Either;
import java.util.function.Function;
import java.util.function.Supplier;

public enum FindCityByIdAPI {
    INSTANCE;

    private static final MockedCityRepositoryKey MOCKED_CITY_REPOSITORY_KEY = MockedCityRepositoryKey.INSTANCE;
    // A reader that always returns a specific error value
    private static final Function<MissingCriticalDependencyError, Reader<AppScopedDependencyLocator, Either<CityNotFoundError, ICity>>> DEPENDENCY_NEVER_FOUND_PATH =
        missingCriticalDependencyError ->
            __ -> Either.left(new CityNotFoundError(missingCriticalDependencyError.getMessage()));
    private static final Function<CityNotFoundByRepositoryError, Reader<AppScopedDependencyLocator, Either<CityNotFoundError, ICity>>> CITY_NEVER_FOUND_BY_ID_PATH =
        cityNotFoundByRepositoryError ->
            __ ->
                Either.left(new CityNotFoundError(cityNotFoundByRepositoryError.getMessage()));
    private static final Function<ICity, Reader<AppScopedDependencyLocator, Either<CityNotFoundError, ICity>>> CITY_FOUND_BY_ID_PATH =
        iCity -> __ -> Either.right(iCity);

    public Reader<AppScopedDependencyLocator, Either<CityNotFoundError, ICity>> execute(
        final CityCriteriaDetails cityCriteriaDetails) {
        long cityId = cityCriteriaDetails.cityId();
        Supplier<Reader<AppScopedDependencyLocator, Either<CityNotFoundError, ICity>>> cityNotFoundPath =
            () -> __ -> Either.left(
                new CityNotFoundError(ErrorMessage.CITY_NOT_FOUND_ERROR_MESSAGE.formatted(cityId)));
        // Compose operations with flatMap to explicitly avoid apply
        return MOCKED_CITY_REPOSITORY_KEY.describeDependencyContext()
            .flatMap(cityNotFoundErrorICityRepositoryEither ->
                cityNotFoundErrorICityRepositoryEither.fold(
                    DEPENDENCY_NEVER_FOUND_PATH,
                    iCityRepository ->
                        iCityRepository.findById(cityId)
                            .flatMap(cityNotFoundByRepositoryErrorOptionEither ->
                                cityNotFoundByRepositoryErrorOptionEither.fold(
                                    CITY_NEVER_FOUND_BY_ID_PATH,
                                    iCityOption ->
                                        Match(iCityOption).of(
                                            Case($Some($()), CITY_FOUND_BY_ID_PATH),
                                            Case($None(), cityNotFoundPath))))));
    }
}
