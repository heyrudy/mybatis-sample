package com.heyrudy.mybatissample.application.context;

import com.heyrudy.mybatissample.domain.CityModelModule.ICity;
import com.heyrudy.mybatissample.domain.CityProgramModule.FindCities;
import com.heyrudy.mybatissample.domain.DomainErrorModule.MissingCriticalDependencyError;
import com.heyrudy.mybatissample.domain.DomainErrorModule.MissingCriticalProgramHandlerError;
import com.heyrudy.mybatissample.gateway.CityDbModule.ICityRepository;
import cyclops.control.Reader;
import io.vavr.control.Either;
import java.util.List;

public enum FindCitiesHandlerKey
    implements ProgramHandlerKey<FindCities, List<ICity>> {
    INSTANCE;

    private static final ProgramHandler<
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
        Either<MissingCriticalProgramHandlerError, ProgramHandler<FindCities, List<ICity>>>
        > lazyLoad() {
        return _ ->
            Either.right(FIND_CITY_HANDLER);
    }

    private static MissingCriticalProgramHandlerError
    toFindCitiesHandlerError(
        MissingCriticalDependencyError error) {
        return new MissingCriticalProgramHandlerError.MissingCriticalCityProgramHandlerError(
            error.message()
        );
    }
}