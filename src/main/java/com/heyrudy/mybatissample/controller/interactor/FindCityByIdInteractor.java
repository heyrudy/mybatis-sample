package com.heyrudy.mybatissample.controller.interactor;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Patterns.$None;
import static io.vavr.Patterns.$Some;

import com.heyrudy.mybatissample.context.AppScopedDependencyLocator;
import com.heyrudy.mybatissample.domain.error.DomainRepositoryError;
import com.heyrudy.mybatissample.domain.error.DomainServiceAPIError;
import com.heyrudy.mybatissample.domain.error.DomainServiceAPIError.CityNotFoundError;
import com.heyrudy.mybatissample.domain.model.city.ICity;
import com.heyrudy.mybatissample.domain.model.common.CityCriteriaDetails;
import com.heyrudy.mybatissample.gateway.db.repository.CityRepository;
import com.heyrudy.mybatissample.gateway.db.repository.ICityRepository;
import cyclops.control.Reader;
import io.vavr.control.Either;
import java.util.function.Function;

public enum FindCityByIdInteractor {
    INSTANCE;

    private static final ICityRepository CITY_REPOSITORY_DEPENDENCY_PATH =
        CityRepository.INSTANCE;
    private static final Function<DomainRepositoryError, String> DOMAIN_REPOSITORY_ERROR_MESSAGE =
        DomainRepositoryError::message;
    private static final Function<DomainRepositoryError, Either<DomainServiceAPIError, ICity>> CITY_NOT_FOUND_BY_ID_PATH =
        DOMAIN_REPOSITORY_ERROR_MESSAGE
            .andThen(DomainServiceAPIError.CityNotFoundError::new)
            .andThen(Either::left);

    public Reader<AppScopedDependencyLocator, Either<DomainServiceAPIError, ICity>> execute(
        final CityCriteriaDetails cityCriteriaDetails) {
        long cityId = cityCriteriaDetails.cityId();
        return CITY_REPOSITORY_DEPENDENCY_PATH.findById(cityId)
            .map(domainRepositoryErrorOptionEither ->
                domainRepositoryErrorOptionEither.fold(
                    CITY_NOT_FOUND_BY_ID_PATH,
                    iCityOption ->
                        Match(iCityOption).of(
                            Case($Some($()), Either::right),
                            Case($None(), () -> Either.left(
                                new DomainServiceAPIError.CityNotFoundError(
                                    CityNotFoundError.ErrorMessage.CITY_NOT_FOUND
                                        .formatted(cityId)))))));
    }
}
