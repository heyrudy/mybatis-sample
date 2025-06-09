package com.heyrudy.mybatissample.controller.interactor;

import com.heyrudy.mybatissample.context.AppScopedDependencyLocator;
import com.heyrudy.mybatissample.domain.error.DomainRepositoryError;
import com.heyrudy.mybatissample.domain.error.DomainServiceAPIError;
import com.heyrudy.mybatissample.domain.model.city.ICity;
import com.heyrudy.mybatissample.gateway.db.repository.CityRepository;
import com.heyrudy.mybatissample.gateway.db.repository.ICityRepository;
import cyclops.control.Reader;
import io.vavr.control.Either;
import java.util.function.Function;

public enum CreateCityInteractor {
    INSTANCE;

    private static final ICityRepository CITY_REPOSITORY_DEPENDENCY_PATH =
        CityRepository.INSTANCE;
    private static final Function<DomainRepositoryError, String> DOMAIN_REPOSITORY_ERROR_MESSAGE =
        DomainRepositoryError::message;
    private static final Function<DomainRepositoryError, Either<DomainServiceAPIError, ICity>> CITY_NOT_SAVED_BY_REPOSITORY_PATH =
        DOMAIN_REPOSITORY_ERROR_MESSAGE
            .andThen(DomainServiceAPIError.CityNotSavedError::new)
            .andThen(Either::left);
    private static final Function<Either<DomainRepositoryError, ICity>, Either<DomainServiceAPIError, ICity>> SAVE_CITY_PATH =
        domainRepositoryErrorICityEither ->
            domainRepositoryErrorICityEither.fold(CITY_NOT_SAVED_BY_REPOSITORY_PATH, Either::right);

    public Reader<AppScopedDependencyLocator, Either<DomainServiceAPIError, ICity>> execute(
        final ICity iCity) {
        return CITY_REPOSITORY_DEPENDENCY_PATH.save(iCity).map(SAVE_CITY_PATH);
    }
}
