package com.heyrudy.mybatissample.application;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Patterns.$None;
import static io.vavr.Patterns.$Some;

import com.heyrudy.mybatissample.application.context.AppScopedDependencyLocator;
import com.heyrudy.mybatissample.domain.DomainRepositoryError;
import com.heyrudy.mybatissample.domain.DomainServiceAPIError;
import com.heyrudy.mybatissample.domain.DomainServiceAPIError.CityNotFoundError;
import com.heyrudy.mybatissample.domain.MissingCriticalDependencyError;
import com.heyrudy.mybatissample.domain.CityModelModule.CityCriteriaDetails;
import com.heyrudy.mybatissample.domain.CityModelModule.ICity;
import com.heyrudy.mybatissample.gateway.db.CityDbModule.CityRepository;
import com.heyrudy.mybatissample.gateway.db.CityDbModule.ICityRepository;
import cyclops.control.Reader;
import io.vavr.control.Either;
import java.util.List;
import java.util.function.Function;

public interface CityInteractorModule {

    ICityRepository CITY_REPOSITORY_DEPENDENCY =
        CityRepository.INSTANCE;
    Function<DomainRepositoryError, String> DOMAIN_REPOSITORY_ERROR_MESSAGE =
        DomainRepositoryError::message;

    enum CreateCityInteractor {
        INSTANCE;

        private static final Function<DomainRepositoryError, Either<DomainServiceAPIError, ICity>> CITY_NOT_SAVED_PATH =
            DOMAIN_REPOSITORY_ERROR_MESSAGE
                .andThen(DomainServiceAPIError.CityNotSavedError::new)
                .andThen(Either::left);
        private static final Function<Either<DomainRepositoryError, ICity>, Either<DomainServiceAPIError, ICity>> MAP_TO_CITY_PATH =
            domainRepositoryErrorICityEither ->
                domainRepositoryErrorICityEither.fold(CITY_NOT_SAVED_PATH, Either::right);

        public Reader<AppScopedDependencyLocator, Either<DomainServiceAPIError, ICity>> execute(
            final ICity iCity) {
            return CITY_REPOSITORY_DEPENDENCY.save(iCity).map(MAP_TO_CITY_PATH);
        }
    }

    enum FindCityByIdInteractor {
        INSTANCE;

        private static final Function<DomainRepositoryError, Either<DomainServiceAPIError, ICity>> CITY_NOT_FOUND_BY_ID_PATH =
            DOMAIN_REPOSITORY_ERROR_MESSAGE
                .andThen(DomainServiceAPIError.CityNotFoundError::new)
                .andThen(Either::left);

        public Reader<AppScopedDependencyLocator, Either<DomainServiceAPIError, ICity>> execute(
            final CityCriteriaDetails cityCriteriaDetails) {
            long cityId = cityCriteriaDetails.cityId();
            return CITY_REPOSITORY_DEPENDENCY.findById(cityId)
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

    enum FindCitiesInteractor {
        INSTANCE;

        private static final Function<MissingCriticalDependencyError, String> MISSING_CRITICAL_DEPENDENCY_ERROR_MESSAGE =
            MissingCriticalDependencyError::message;
        private static final Function<MissingCriticalDependencyError, Either<DomainServiceAPIError, List<ICity>>> CRITICAL_DSL_CONTEXT_NOT_FOUND_PATH =
            MISSING_CRITICAL_DEPENDENCY_ERROR_MESSAGE
                .andThen(DomainServiceAPIError.CitiesNotFoundError::new)
                .andThen(Either::left);
        private static final Function<Either<MissingCriticalDependencyError, List<ICity>>, Either<DomainServiceAPIError, List<ICity>>> MAP_TO_CITIES_PATH =
            missingCriticalDependencyErrorListEither ->
                missingCriticalDependencyErrorListEither.fold(
                    CRITICAL_DSL_CONTEXT_NOT_FOUND_PATH, Either::right);

        public Reader<AppScopedDependencyLocator, Either<DomainServiceAPIError, List<ICity>>> execute() {
            return CITY_REPOSITORY_DEPENDENCY.findAll().map(MAP_TO_CITIES_PATH);
        }
    }
}
