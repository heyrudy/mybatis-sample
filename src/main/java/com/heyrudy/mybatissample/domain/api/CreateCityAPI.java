package com.heyrudy.mybatissample.domain.api;

import com.heyrudy.mybatissample.context.AppScopedDependencyLocator;
import com.heyrudy.mybatissample.context.CityRepositoryKey;
import com.heyrudy.mybatissample.domain.model.city.ICity;
import com.heyrudy.mybatissample.domain.model.error.CityNotSavedByRepositoryError;
import com.heyrudy.mybatissample.domain.model.error.CriticalRepositoryNotFoundByDependencyLocatorError;
import com.heyrudy.mybatissample.domain.model.error.MissingCriticalDependencyError;
import com.heyrudy.mybatissample.domain.spi.ICityRepository;
import cyclops.control.Reader;
import io.vavr.control.Either;
import java.util.function.Function;

public enum CreateCityAPI {
    INSTANCE;

    public Reader<AppScopedDependencyLocator, Either<CriticalRepositoryNotFoundByDependencyLocatorError, ICity>> execute(
        final ICity iCity) {
        // Define error mapping functions
        Function<MissingCriticalDependencyError, CriticalRepositoryNotFoundByDependencyLocatorError> mapDependencyError =
            missingCriticalDependencyError ->
                new CriticalRepositoryNotFoundByDependencyLocatorError(
                    missingCriticalDependencyError.getMessage());
        Function<CityNotSavedByRepositoryError, CriticalRepositoryNotFoundByDependencyLocatorError> mapSaveError =
            cityNotSavedByRepositoryError ->
                new CriticalRepositoryNotFoundByDependencyLocatorError(
                    cityNotSavedByRepositoryError.getMessage());
        // A reader that always returns a specific error value
        Function<CriticalRepositoryNotFoundByDependencyLocatorError, Reader<AppScopedDependencyLocator, Either<CriticalRepositoryNotFoundByDependencyLocatorError, ICity>>>
            constantErrorReader = criticalRepositoryNotFoundByDependencyLocatorError ->
            __ -> Either.left(criticalRepositoryNotFoundByDependencyLocatorError);
        // Function to convert a repository into a save operation
        Function<ICityRepository, Reader<AppScopedDependencyLocator, Either<CriticalRepositoryNotFoundByDependencyLocatorError, ICity>>>
            saveWithRepository = iCityRepository ->
            iCityRepository.save(iCity).map(either -> either.mapLeft(mapSaveError));
        // Compose operations with flatMap to explicitly avoid apply
        return CityRepositoryKey.INSTANCE.describeDependencyContext()
            .map(iCityRepositoryEither ->
                iCityRepositoryEither.mapLeft(mapDependencyError))
            .flatMap(criticalRepositoryNotFoundByDependencyLocatorErrorICityRepositoryEither ->
                criticalRepositoryNotFoundByDependencyLocatorErrorICityRepositoryEither.fold(
                    constantErrorReader, saveWithRepository));
    }
}
