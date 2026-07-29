package com.heyrudy.mybatissample.application.context;

import com.heyrudy.mybatissample.domain.CityModelModule;
import com.heyrudy.mybatissample.gateway.CityDbModule;
import cyclops.control.Reader;
import io.vavr.control.Either;
import io.vavr.control.Option;
import java.util.List;

public interface CityContextModule
    extends CityModelModule
    , CityDbModule {

    sealed interface CityProgramAST
        permits Pure
        , CapabilityProgram {

    }

    record Pure<A>(
        A value
    ) implements CityProgramAST {

    }

    sealed interface CapabilityProgram
        extends CityProgramAST
        permits SaveCity
        , FindCity
        , FindCities
        , AuditAction {

    }

    record SaveCity(
        ICity city
    ) implements CapabilityProgram {

    }

    record FindCity(
        Long cityId
    ) implements CapabilityProgram {

    }

    record FindCities(
    ) implements CapabilityProgram {

    }


    sealed interface CityProgramHandlerKey<P extends CityProgramAST, R>
        extends ProgramHandlerKey<P, R>
        permits SaveCityHandlerKey
        , FindCityHandlerKey
        , FindCitiesHandlerKey {

    }

    enum SaveCityHandlerKey
        implements CityProgramHandlerKey<SaveCity, ICity> {
        INSTANCE;

        private static final CityProgramHandler<
            SaveCity, ICity
            > SAVE_CITY_HANDLER =
            saveCity ->
                ProgramHandlerKey.resolveAndThen(
                    CityRepositoryKey.INSTANCE,
                    SaveCityHandlerKey::toSaveCityHandlerError,
                    repository ->
                        repository.save(
                            saveCity.city()
                        )
                );

        @Override
        public Reader<
            AppScopedDependencyLocator,
            Either<MissingCriticalProgramHandlerError, CityProgramHandler<SaveCity, ICity>>
            > lazyLoad() {
            return _ ->
                Either.right(SAVE_CITY_HANDLER);
        }

        private static MissingCriticalProgramHandlerError toSaveCityHandlerError(
            MissingCriticalDependencyError error) {
            return new MissingCriticalProgramHandlerError.MissingCriticalCityProgramHandlerError(
                error.message()
            );
        }
    }

    enum FindCityHandlerKey
        implements CityProgramHandlerKey<FindCity, Option<ICity>> {
        INSTANCE;

        private static final CityProgramHandler<
            FindCity, Option<ICity>
            > FIND_CITY_HANDLER =
            findCity ->
                ProgramHandlerKey.resolveAndThen(
                    CityRepositoryKey.INSTANCE,
                    FindCityHandlerKey::toFindCityHandlerError,
                    repository ->
                        repository.findById(
                            findCity.cityId()
                        )
                );

        @Override
        public Reader<
            AppScopedDependencyLocator,
            Either<MissingCriticalProgramHandlerError, CityProgramHandler<FindCity, Option<ICity>>>
            > lazyLoad() {
            return _ ->
                Either.right(FIND_CITY_HANDLER);
        }

        private static MissingCriticalProgramHandlerError toFindCityHandlerError(
            MissingCriticalDependencyError error) {
            return new MissingCriticalProgramHandlerError.MissingCriticalCityProgramHandlerError(
                error.message()
            );
        }
    }

    enum FindCitiesHandlerKey
        implements CityProgramHandlerKey<FindCities, List<ICity>> {
        INSTANCE;

        private static final CityProgramHandler<
            FindCities, List<ICity>
            > FIND_CITY_HANDLER =
            _ ->
                ProgramHandlerKey.resolveAndThen(
                    CityRepositoryKey.INSTANCE,
                    FindCitiesHandlerKey::toFindCitiesHandlerError,
                    ICityRepository::findAll
                );

        @Override
        public Reader<
            AppScopedDependencyLocator,
            Either<MissingCriticalProgramHandlerError, CityProgramHandler<FindCities, List<ICity>>>
            > lazyLoad() {
            return _ ->
                Either.right(FIND_CITY_HANDLER);
        }

        private static MissingCriticalProgramHandlerError toFindCitiesHandlerError(
            MissingCriticalDependencyError error) {
            return new MissingCriticalProgramHandlerError.MissingCriticalCityProgramHandlerError(
                error.message()
            );
        }
    }

    @FunctionalInterface
    interface CityProgramHandler<P extends CityProgramAST, R> {

        Reader<
            AppScopedDependencyLocator,
            Either<DomainError, R>
            > handle(P program);
    }
}
