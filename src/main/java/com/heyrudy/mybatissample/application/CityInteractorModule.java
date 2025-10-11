package com.heyrudy.mybatissample.application;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Patterns.$None;
import static io.vavr.Patterns.$Some;

import com.heyrudy.mybatissample.application.context.AppScopedDependencyLocator;
import com.heyrudy.mybatissample.domain.CityModelModule;
import com.heyrudy.mybatissample.domain.DomainErrorModule;
import com.heyrudy.mybatissample.domain.DomainRepositoryError;
import com.heyrudy.mybatissample.gateway.db.CityDbModule;
import cyclops.control.Reader;
import io.vavr.control.Either;
import java.util.List;
import java.util.function.Function;

public interface CityInteractorModule
    extends CityModelModule,
    CityDbModule,
    DomainErrorModule {

    ICityRepository CITY_REPOSITORY_DEPENDENCY =
        CityRepository.INSTANCE;
    Function<DomainRepositoryError, String> DOMAIN_REPOSITORY_ERROR_MESSAGE =
        DomainRepositoryError::message;

    enum CreateCityInteractor {
        INSTANCE;

        private static final Function<DomainRepositoryError, Either<DomainErrorModule.DomainServiceAPIError, ICity>> CITY_NOT_SAVED =
            DOMAIN_REPOSITORY_ERROR_MESSAGE
                .andThen(DomainErrorModule.DomainServiceAPIError.CityNotSavedError::new)
                .andThen(Either::left);
        private static final Function<Either<DomainRepositoryError, ICity>, Either<DomainErrorModule.DomainServiceAPIError, ICity>> MAP_TO_CITY =
            domainRepositoryErrorICityEither ->
                domainRepositoryErrorICityEither.fold(CITY_NOT_SAVED, Either::right);

        public Reader<AppScopedDependencyLocator, Either<DomainErrorModule.DomainServiceAPIError, ICity>> execute(
            final ICity iCity) {
            return CITY_REPOSITORY_DEPENDENCY.save(iCity).map(MAP_TO_CITY);
        }
    }

    enum FindCityByIdInteractor {
        INSTANCE;

        private static final Function<DomainRepositoryError, Either<DomainErrorModule.DomainServiceAPIError, ICity>> CITY_NOT_FOUND_BY_ID =
            DOMAIN_REPOSITORY_ERROR_MESSAGE
                .andThen(DomainErrorModule.DomainServiceAPIError.CityNotFoundError::new)
                .andThen(Either::left);

        public Reader<AppScopedDependencyLocator, Either<DomainErrorModule.DomainServiceAPIError, ICity>> execute(
            final CityCriteriaDetails cityCriteriaDetails) {
            long cityId = cityCriteriaDetails.cityId();
            return CITY_REPOSITORY_DEPENDENCY.findById(cityId)
                .map(domainRepositoryErrorOptionEither ->
                    domainRepositoryErrorOptionEither.fold(
                        CITY_NOT_FOUND_BY_ID,
                        iCityOption ->
                            Match(iCityOption).of(
                                Case($Some($()), Either::right),
                                Case($None(), () -> Either.left(
                                    new DomainErrorModule.DomainServiceAPIError.CityNotFoundError(
                                        DomainErrorModule.DomainServiceAPIError.CityNotFoundError.ErrorMessage.CITY_NOT_FOUND
                                            .formatted(cityId)))))));
        }
    }

    enum FindCitiesInteractor {
        INSTANCE;

        private static final Function<DomainErrorModule.MissingCriticalDependencyError, String> MISSING_CRITICAL_DEPENDENCY_ERROR_MESSAGE =
            DomainErrorModule.MissingCriticalDependencyError::message;
        private static final Function<DomainErrorModule.MissingCriticalDependencyError, Either<DomainErrorModule.DomainServiceAPIError, List<ICity>>> CRITICAL_DSL_CONTEXT_NOT_FOUND =
            MISSING_CRITICAL_DEPENDENCY_ERROR_MESSAGE
                .andThen(DomainErrorModule.DomainServiceAPIError.CitiesNotFoundError::new)
                .andThen(Either::left);
        private static final Function<Either<MissingCriticalDependencyError, List<ICity>>, Either<DomainErrorModule.DomainServiceAPIError, List<ICity>>> MAP_TO_CITIES =
            missingCriticalDependencyErrorListEither ->
                missingCriticalDependencyErrorListEither.fold(
                    CRITICAL_DSL_CONTEXT_NOT_FOUND, Either::right);

        public Reader<AppScopedDependencyLocator, Either<DomainErrorModule.DomainServiceAPIError, List<ICity>>> execute() {
            return CITY_REPOSITORY_DEPENDENCY.findAll().map(MAP_TO_CITIES);
        }
    }
}
