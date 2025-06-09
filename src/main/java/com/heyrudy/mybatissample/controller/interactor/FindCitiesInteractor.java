package com.heyrudy.mybatissample.controller.interactor;

import com.heyrudy.mybatissample.context.AppScopedDependencyLocator;
import com.heyrudy.mybatissample.domain.error.DomainServiceAPIError;
import com.heyrudy.mybatissample.domain.error.MissingCriticalDependencyError;
import com.heyrudy.mybatissample.domain.model.city.ICity;
import com.heyrudy.mybatissample.gateway.db.repository.CityRepository;
import com.heyrudy.mybatissample.gateway.db.repository.ICityRepository;
import cyclops.control.Reader;
import io.vavr.control.Either;
import java.util.List;
import java.util.function.Function;

public enum FindCitiesInteractor {
    INSTANCE;

    private static final ICityRepository CITY_REPOSITORY_DEPENDENCY_PATH =
        CityRepository.INSTANCE;
    private static final Function<MissingCriticalDependencyError, String> MISSING_CRITICAL_DEPENDENCY_ERROR_MESSAGE =
        MissingCriticalDependencyError::message;
    private static final Function<MissingCriticalDependencyError, Either<DomainServiceAPIError, List<ICity>>> CRITICAL_DSL_CONTEXT_NOT_FOUND_PATH =
        MISSING_CRITICAL_DEPENDENCY_ERROR_MESSAGE
            .andThen(DomainServiceAPIError.CitiesNotFoundError::new)
            .andThen(Either::left);
    private static final Function<Either<MissingCriticalDependencyError, List<ICity>>, Either<DomainServiceAPIError, List<ICity>>> FIND_CITIES_PATH =
        missingCriticalDependencyErrorListEither ->
            missingCriticalDependencyErrorListEither.fold(
                CRITICAL_DSL_CONTEXT_NOT_FOUND_PATH, Either::right);

    public Reader<AppScopedDependencyLocator, Either<DomainServiceAPIError, List<ICity>>> execute() {
        return CITY_REPOSITORY_DEPENDENCY_PATH.findAll().map(FIND_CITIES_PATH);
    }
}
