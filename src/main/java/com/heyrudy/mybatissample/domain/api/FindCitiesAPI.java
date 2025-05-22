package com.heyrudy.mybatissample.domain.api;

import com.heyrudy.mybatissample.context.AppScopedDependencyLocator;
import com.heyrudy.mybatissample.context.MockedCityRepositoryKey;
import com.heyrudy.mybatissample.domain.model.city.ICity;
import com.heyrudy.mybatissample.domain.model.error.CitiesNotFoundError;
import com.heyrudy.mybatissample.domain.model.error.DomainServiceAPIError;
import com.heyrudy.mybatissample.domain.model.error.MissingCriticalDependencyError;
import com.heyrudy.mybatissample.domain.spi.ICityRepository;
import cyclops.control.Reader;
import io.vavr.control.Either;
import java.util.List;
import java.util.function.Function;

public enum FindCitiesAPI {
    INSTANCE;

    private static final Reader<AppScopedDependencyLocator, Either<MissingCriticalDependencyError, ICityRepository>> GET_MOCKED_CITY_REPOSITORY_DEPENDENCY_PATH =
        MockedCityRepositoryKey.INSTANCE.describeDependencyContext();
    // A reader that always returns a specific error value
    private static final Function<MissingCriticalDependencyError, Reader<AppScopedDependencyLocator, Either<DomainServiceAPIError, List<ICity>>>> DEPENDENCY_NEVER_FOUND_PATH =
        missingCriticalDependencyError ->
            __ ->
                Either.left(new CitiesNotFoundError(missingCriticalDependencyError.message()));
    private static final Function<MissingCriticalDependencyError, Either<DomainServiceAPIError, List<ICity>>> CRITICAL_DSL_CONTEXT_NOT_FOUND_BY_DEPENDENCY_LOCATOR_PATH =
        missingCriticalDependencyError ->
            Either.left(new CitiesNotFoundError(missingCriticalDependencyError.message()));
    private static final Function<Either<MissingCriticalDependencyError, List<ICity>>, Either<DomainServiceAPIError, List<ICity>>> FIND_CITIES_PATH =
        missingCriticalDependencyErrorListEither ->
            missingCriticalDependencyErrorListEither.fold(
                CRITICAL_DSL_CONTEXT_NOT_FOUND_BY_DEPENDENCY_LOCATOR_PATH, Either::right);
    private static final Function<ICityRepository, Reader<AppScopedDependencyLocator, Either<DomainServiceAPIError, List<ICity>>>> FIND_CITIES_BY_REPOSITORY_PATH =
        iCityRepository -> iCityRepository.findAll().map(FIND_CITIES_PATH);

    public Reader<AppScopedDependencyLocator, Either<DomainServiceAPIError, List<ICity>>> execute() {
        return GET_MOCKED_CITY_REPOSITORY_DEPENDENCY_PATH
            .flatMap(missingCriticalDependencyErrorICityRepositoryEither ->
                missingCriticalDependencyErrorICityRepositoryEither.fold(
                    DEPENDENCY_NEVER_FOUND_PATH, FIND_CITIES_BY_REPOSITORY_PATH));
    }
}
