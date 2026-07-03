package com.heyrudy.mybatissample.application.context;

import com.heyrudy.mybatissample.domain.CityModelModule.ICity;
import com.heyrudy.mybatissample.domain.CityProgramModule.SaveCity;
import com.heyrudy.mybatissample.domain.DomainErrorModule;
import com.heyrudy.mybatissample.domain.DomainErrorModule.MissingCriticalDependencyError;
import com.heyrudy.mybatissample.domain.DomainErrorModule.MissingCriticalProgramHandlerError;
import cyclops.control.Reader;
import io.vavr.control.Either;

public enum SaveCityHandlerKey
    implements ProgramHandlerKey<SaveCity, ICity> {
    INSTANCE;

    private static final ProgramHandler<
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
        Either<DomainErrorModule.MissingCriticalProgramHandlerError, ProgramHandler<SaveCity, ICity>>
        > lazyLoad() {
        return _ ->
            Either.right(SAVE_CITY_HANDLER);
    }

    private static MissingCriticalProgramHandlerError
    toSaveCityHandlerError(
        MissingCriticalDependencyError error) {
        return new MissingCriticalProgramHandlerError.MissingCriticalCityProgramHandlerError(
            error.message()
        );
    }
}