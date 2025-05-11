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
import com.heyrudy.mybatissample.domain.spi.ICityRepository;
import cyclops.control.Reader;
import io.vavr.control.Either;
import java.util.function.Function;
import java.util.function.Supplier;

public enum FindCityByIdAPI {
    INSTANCE;

    private static final MockedCityRepositoryKey MOCKED_CITY_REPOSITORY_KEY = MockedCityRepositoryKey.INSTANCE;
    // Define error mapping functions
    private static final Function<MissingCriticalDependencyError, CityNotFoundError> MAP_DEPENDENCY_ERROR =
        missingCriticalDependencyError ->
            new CityNotFoundError(missingCriticalDependencyError.getMessage());
    private static final Function<Either<? extends MissingCriticalDependencyError, ICityRepository>, Either<CityNotFoundError, ICityRepository>> EITHER_TO_MAP_DEPENDENCY_ERROR =
        iCityRepositoryEither ->
            iCityRepositoryEither.mapLeft(MAP_DEPENDENCY_ERROR);
    private static final Function<CityNotFoundError, Reader<AppScopedDependencyLocator, Either<CityNotFoundError, ICity>>> CITY_NOT_FOUND_ERROR =
        cityNotFoundError ->
            __ -> Either.left(cityNotFoundError);
    // A reader that always returns a specific error value
    private static final Function<CityNotFoundByRepositoryError, Reader<AppScopedDependencyLocator, Either<CityNotFoundError, ICity>>> CITY_NOT_FOUND_BY_REPOSITORY_ERROR =
        cityNotFoundByRepositoryError ->
            __ ->
                Either.left(new CityNotFoundError(cityNotFoundByRepositoryError.getMessage()));
    private static final Function<ICity, Reader<AppScopedDependencyLocator, Either<CityNotFoundError, ICity>>> FOUND_SUCCESS =
        iCity -> __ -> Either.right(iCity);

    public Reader<AppScopedDependencyLocator, Either<CityNotFoundError, ICity>> execute(
        final CityCriteriaDetails cityCriteriaDetails) {
        long cityId = cityCriteriaDetails.cityId();
        Supplier<Reader<AppScopedDependencyLocator, Either<CityNotFoundError, ICity>>> notFoundError =
            () -> __ -> Either.left(
                new CityNotFoundError(ErrorMessage.CITY_NOT_FOUND_ERROR_MESSAGE.formatted(cityId)));
        // Compose operations with flatMap to explicitly avoid apply
        return MOCKED_CITY_REPOSITORY_KEY.describeDependencyContext()
            .map(EITHER_TO_MAP_DEPENDENCY_ERROR)
            .flatMap(cityNotFoundErrorICityRepositoryEither ->
                cityNotFoundErrorICityRepositoryEither.fold(
                    CITY_NOT_FOUND_ERROR,
                    iCityRepository ->
                        iCityRepository.findById(cityId)
                            .flatMap(cityNotFoundByRepositoryErrorOptionEither ->
                                cityNotFoundByRepositoryErrorOptionEither.fold(
                                    CITY_NOT_FOUND_BY_REPOSITORY_ERROR,
                                    iCityOption ->
                                        Match(iCityOption).of(
                                            Case($Some($()), FOUND_SUCCESS),
                                            Case($None(), notFoundError))))));
    }
}
