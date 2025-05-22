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
import com.heyrudy.mybatissample.domain.error.CityNotFoundError;
import com.heyrudy.mybatissample.domain.error.CityNotFoundError.ErrorMessage;
import com.heyrudy.mybatissample.domain.error.DomainRepositoryError;
import com.heyrudy.mybatissample.domain.error.DomainServiceAPIError;
import com.heyrudy.mybatissample.domain.error.MissingCriticalDependencyError;
import com.heyrudy.mybatissample.domain.spi.ICityRepository;
import cyclops.control.Reader;
import io.vavr.control.Either;
import java.util.function.Function;
import java.util.function.Supplier;

public enum FindCityByIdAPI {
    INSTANCE;

    private static final Reader<AppScopedDependencyLocator, Either<MissingCriticalDependencyError, ICityRepository>> GET_MOCKED_CITY_REPOSITORY_DEPENDENCY_PATH =
        MockedCityRepositoryKey.INSTANCE.describeDependencyContext();
    // A reader that always returns a specific error value
    private static final Function<MissingCriticalDependencyError, Reader<AppScopedDependencyLocator, Either<DomainServiceAPIError, ICity>>> DEPENDENCY_NEVER_FOUND_PATH =
        missingCriticalDependencyError ->
            __ -> Either.left(new CityNotFoundError(missingCriticalDependencyError.message()));
    private static final Function<DomainRepositoryError, Reader<AppScopedDependencyLocator, Either<DomainServiceAPIError, ICity>>> CITY_NEVER_FOUND_BY_ID_PATH =
        domainRepositoryError ->
            __ ->
                Either.left(new CityNotFoundError(domainRepositoryError.message()));
    private static final Function<ICity, Reader<AppScopedDependencyLocator, Either<DomainServiceAPIError, ICity>>> CITY_FOUND_BY_ID_PATH =
        iCity -> __ -> Either.right(iCity);

    public Reader<AppScopedDependencyLocator, Either<DomainServiceAPIError, ICity>> execute(
        final CityCriteriaDetails cityCriteriaDetails) {
        long cityId = cityCriteriaDetails.cityId();
        Supplier<Reader<AppScopedDependencyLocator, Either<DomainServiceAPIError, ICity>>> cityNotFoundPath =
            () -> __ -> Either.left(
                new CityNotFoundError(ErrorMessage.CITY_NOT_FOUND_ERROR_MESSAGE.formatted(cityId)));
        // Compose operations with flatMap to explicitly avoid apply
        return GET_MOCKED_CITY_REPOSITORY_DEPENDENCY_PATH
            .flatMap(missingCriticalDependencyErrorICityRepositoryEither ->
                missingCriticalDependencyErrorICityRepositoryEither.fold(
                    DEPENDENCY_NEVER_FOUND_PATH,
                    iCityRepository ->
                        iCityRepository.findById(cityId)
                            .flatMap(domainRepositoryErrorOptionEither ->
                                domainRepositoryErrorOptionEither.fold(
                                    CITY_NEVER_FOUND_BY_ID_PATH,
                                    iCityOption ->
                                        Match(iCityOption).of(
                                            Case($Some($()), CITY_FOUND_BY_ID_PATH),
                                            Case($None(), cityNotFoundPath))))));
    }
}
