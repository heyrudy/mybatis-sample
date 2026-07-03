package com.heyrudy.mybatissample.application.context;

import com.heyrudy.mybatissample.domain.CityModelModule.ICity;
import com.heyrudy.mybatissample.domain.CityProgramModule.FindCity;
import com.heyrudy.mybatissample.domain.DomainErrorModule.MissingCriticalDependencyError;
import com.heyrudy.mybatissample.domain.DomainErrorModule.MissingCriticalProgramHandlerError;
import cyclops.control.Reader;
import io.vavr.control.Either;
import io.vavr.control.Option;

public enum FindCityHandlerKey
    implements ProgramHandlerKey<FindCity, Option<ICity>> {
    INSTANCE;

    private static final ProgramHandler<
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
        Either<MissingCriticalProgramHandlerError, ProgramHandler<FindCity, Option<ICity>>>
        > lazyLoad() {
        return _ ->
            Either.right(FIND_CITY_HANDLER);
    }

    private static MissingCriticalProgramHandlerError
    toFindCityHandlerError(
        MissingCriticalDependencyError error) {
        return new MissingCriticalProgramHandlerError.MissingCriticalCityProgramHandlerError(
            error.message()
        );
    }
}