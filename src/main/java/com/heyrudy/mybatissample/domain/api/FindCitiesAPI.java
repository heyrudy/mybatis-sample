package com.heyrudy.mybatissample.domain.api;

import com.heyrudy.mybatissample.context.AppScopedDependencyLocator;
import com.heyrudy.mybatissample.context.CityRepositoryKey;
import com.heyrudy.mybatissample.domain.error.CitiesNotFoundError;
import com.heyrudy.mybatissample.domain.error.DomainServiceAPIError;
import com.heyrudy.mybatissample.domain.error.MissingCriticalDependencyError;
import com.heyrudy.mybatissample.domain.model.city.ICity;
import com.heyrudy.mybatissample.domain.spi.ICityRepository;
import cyclops.control.Reader;
import io.vavr.control.Either;
import java.util.List;
import java.util.function.Function;

public enum FindCitiesAPI {
    INSTANCE;

    private static final Reader<AppScopedDependencyLocator, Either<MissingCriticalDependencyError, ICityRepository>> CITY_REPOSITORY_DEPENDENCY_LAZY_LOADED_PATH =
        CityRepositoryKey.INSTANCE.lazyLoad();
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
        return CITY_REPOSITORY_DEPENDENCY_LAZY_LOADED_PATH
            .flatMap(missingCriticalDependencyErrorICityRepositoryEither ->
                missingCriticalDependencyErrorICityRepositoryEither.fold(
                    DEPENDENCY_NEVER_FOUND_PATH, FIND_CITIES_BY_REPOSITORY_PATH));
    }
}
