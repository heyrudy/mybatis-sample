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
import com.heyrudy.mybatissample.gateway.AuditModule;
import com.heyrudy.mybatissample.gateway.CityDbModule;
import cyclops.control.Reader;
import io.vavr.control.Either;
import java.util.List;
import java.util.function.Function;

public interface CityInteractorModule
    extends CityModelModule
    , CityDbModule
    , DomainErrorModule
    , AuditModule {

    IAuditSPI AUDIT_DEPENDENCY =
        AuditAdapterResolver.INSTANCE.resolve();
    ICityRepository CITY_REPOSITORY_DEPENDENCY =
        CityRepository.INSTANCE;
    Function<DomainRepositoryError, String> DOMAIN_REPOSITORY_ERROR_MESSAGE =
        DomainRepositoryError::message;

    enum CreateCityInteractor {
        INSTANCE;

        private static final Function<DomainRepositoryError, DomainErrorModule.DomainServiceAPIError> CITY_NOT_SAVED =
            DOMAIN_REPOSITORY_ERROR_MESSAGE
                .andThen(DomainErrorModule.DomainServiceAPIError.CityNotSavedError::new);
        private static final Function<Either<DomainRepositoryError, ICity>, Either<DomainErrorModule.DomainServiceAPIError, ICity>> MAP_TO_CITY =
            domainRepositoryErrorICityEither ->
                domainRepositoryErrorICityEither.bimap(CITY_NOT_SAVED, Function.identity());

        private static Either<DomainErrorModule.DomainServiceAPIError, ICity> auditAndReturn(
            final Either<DomainErrorModule.DomainServiceAPIError, ICity> result) {
            result.fold(
                _ -> {
                    AUDIT_DEPENDENCY.auditAction(
                        AuditContext.of(
                            AuditContextMutatorStages.INSTANCE.outcome(result),
                            AuditContextMutatorStages.INSTANCE.errorMapper(
                                DomainServiceAPIError::message),
                            AuditContextMutatorStages.INSTANCE.phase(Phase.IN_PROGRESS),
                            AuditContextMutatorStages.INSTANCE.caller(CreateCityInteractor.class)));
                    return null;
                },
                _ -> {
                    AUDIT_DEPENDENCY.auditAction(
                        AuditContext.of(
                            AuditContextMutatorStages.INSTANCE.outcome(result),
                            AuditContextMutatorStages.INSTANCE.errorMapper(
                                DomainServiceAPIError::message),
                            AuditContextMutatorStages.INSTANCE.phase(Phase.END),
                            AuditContextMutatorStages.INSTANCE.caller(CreateCityInteractor.class)));
                    return null;
                }
            );
            return result;
        }

        public Reader<AppScopedDependencyLocator, Either<DomainErrorModule.DomainServiceAPIError, ICity>> execute(
            final ICity iCity) {
            AUDIT_DEPENDENCY.auditAction(
                AuditContext.of(
                    AuditContextMutatorStages.INSTANCE.caller(CreateCityInteractor.class)));
            return CITY_REPOSITORY_DEPENDENCY
                .save(iCity)
                .map(MAP_TO_CITY)
                .map(CreateCityInteractor::auditAndReturn);
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
            AUDIT_DEPENDENCY.auditAction(
                AuditContext.of(
                    AuditContextMutatorStages.INSTANCE.caller(FindCityByIdInteractor.class)));
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

        private static final Function<DomainRepositoryError, DomainErrorModule.DomainServiceAPIError> CITIES_NOT_FOUND =
            DOMAIN_REPOSITORY_ERROR_MESSAGE
                .andThen(DomainErrorModule.DomainServiceAPIError.CitiesNotFoundError::new);
        private static final Function<Either<DomainRepositoryError, List<ICity>>, Either<DomainErrorModule.DomainServiceAPIError, List<ICity>>> MAP_TO_CITIES =
            domainRepositoryErrorICityListEither ->
                domainRepositoryErrorICityListEither.bimap(CITIES_NOT_FOUND, Function.identity());

        public Reader<AppScopedDependencyLocator, Either<DomainErrorModule.DomainServiceAPIError, List<ICity>>> execute() {
            AUDIT_DEPENDENCY.auditAction(
                AuditContext.of(
                    AuditContextMutatorStages.INSTANCE.caller(FindCitiesInteractor.class)));
            return CITY_REPOSITORY_DEPENDENCY.findAll().map(MAP_TO_CITIES);
        }
    }
}