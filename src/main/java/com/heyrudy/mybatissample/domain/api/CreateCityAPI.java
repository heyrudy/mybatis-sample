package com.heyrudy.mybatissample.domain.api;

import com.heyrudy.mybatissample.context.AppScopedDependencyLocator;
import com.heyrudy.mybatissample.context.MockedCityRepositoryKey;
import com.heyrudy.mybatissample.domain.model.city.ICity;
import com.heyrudy.mybatissample.domain.error.CityNotSavedError;
import com.heyrudy.mybatissample.domain.error.DomainRepositoryError;
import com.heyrudy.mybatissample.domain.error.DomainServiceAPIError;
import com.heyrudy.mybatissample.domain.error.MissingCriticalDependencyError;
import com.heyrudy.mybatissample.domain.spi.ICityRepository;
import cyclops.control.Reader;
import io.vavr.control.Either;
import java.util.function.Function;

public enum CreateCityAPI {
    INSTANCE;

    private static final Reader<AppScopedDependencyLocator, Either<MissingCriticalDependencyError, ICityRepository>> GET_MOCKED_CITY_REPOSITORY_DEPENDENCY_PATH =
        MockedCityRepositoryKey.INSTANCE.describeDependencyContext();
    // A reader that always returns a specific error value
    private static final Function<MissingCriticalDependencyError, Reader<AppScopedDependencyLocator, Either<DomainServiceAPIError, ICity>>> DEPENDENCY_NEVER_FOUND_PATH =
        missingCriticalDependencyError ->
            __ ->
                Either.left(new CityNotSavedError(missingCriticalDependencyError.message()));
    private static final Function<DomainRepositoryError, Either<DomainServiceAPIError, ICity>> CITY_NOT_SAVED_BY_REPOSITORY_PATH =
        domainRepositoryError ->
            Either.left(new CityNotSavedError(domainRepositoryError.message()));
    private static final Function<Either<DomainRepositoryError, ICity>, Either<DomainServiceAPIError, ICity>> SAVE_CITY_PATH =
        domainRepositoryErrorICityEither ->
            domainRepositoryErrorICityEither.fold(
                CITY_NOT_SAVED_BY_REPOSITORY_PATH, Either::right);

    public Reader<AppScopedDependencyLocator, Either<DomainServiceAPIError, ICity>> execute(
        final ICity iCity) {
        return GET_MOCKED_CITY_REPOSITORY_DEPENDENCY_PATH
            .flatMap(missingCriticalDependencyErrorICityRepositoryEither ->
                missingCriticalDependencyErrorICityRepositoryEither.fold(
                    DEPENDENCY_NEVER_FOUND_PATH,
                    iCityRepository -> iCityRepository.save(iCity).map(SAVE_CITY_PATH)));
    }
}
