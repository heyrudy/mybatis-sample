package com.heyrudy.mybatissample.domain.api;

import com.heyrudy.mybatissample.context.AppScopedDependencyLocator;
import com.heyrudy.mybatissample.context.MockedCityRepositoryKey;
import com.heyrudy.mybatissample.domain.model.city.ICity;
import com.heyrudy.mybatissample.domain.model.error.CriticalDSLContextNotFoundByDependencyLocatorError;
import com.heyrudy.mybatissample.domain.model.error.CriticalRepositoryNotFoundByDependencyLocatorError;
import com.heyrudy.mybatissample.domain.model.error.MissingCriticalDependencyError;
import com.heyrudy.mybatissample.domain.spi.ICityRepository;
import cyclops.control.Reader;
import io.vavr.control.Either;
import java.util.List;
import java.util.function.Function;

public enum FindCitiesAPI {
    INSTANCE;

    public Reader<AppScopedDependencyLocator, Either<CriticalRepositoryNotFoundByDependencyLocatorError, List<ICity>>> execute() {
        // Define error mapping functions
        Function<MissingCriticalDependencyError, CriticalRepositoryNotFoundByDependencyLocatorError> mapDependencyError =
            missingCriticalDependencyError ->
                new CriticalRepositoryNotFoundByDependencyLocatorError(
                    missingCriticalDependencyError.getMessage());
        Function<CriticalDSLContextNotFoundByDependencyLocatorError, CriticalRepositoryNotFoundByDependencyLocatorError> mapDSLError =
            criticalDSLContextNotFoundByDependencyLocatorError ->
                new CriticalRepositoryNotFoundByDependencyLocatorError(
                    criticalDSLContextNotFoundByDependencyLocatorError.getMessage());
        // A reader that always returns a specific error value
        Function<CriticalRepositoryNotFoundByDependencyLocatorError, Reader<AppScopedDependencyLocator, Either<CriticalRepositoryNotFoundByDependencyLocatorError, List<ICity>>>>
            constantErrorReader = criticalRepositoryNotFoundByDependencyLocatorError ->
            __ -> Either.left(criticalRepositoryNotFoundByDependencyLocatorError);
        // Function to convert a repository to a findAll operation
        Function<ICityRepository, Reader<AppScopedDependencyLocator, Either<CriticalRepositoryNotFoundByDependencyLocatorError, List<ICity>>>>
            findAllWithRepository = iCityRepository ->
            iCityRepository.findAll()
                .map(criticalDSLContextNotFoundByDependencyLocatorErrorListEither ->
                    criticalDSLContextNotFoundByDependencyLocatorErrorListEither.mapLeft(
                        mapDSLError));
        // Compose operations with flatMap to explicitly avoid apply
        return MockedCityRepositoryKey.INSTANCE.describeDependencyContext()
            .map(iCityRepositoryEither ->
                iCityRepositoryEither.mapLeft(mapDependencyError))
            .flatMap(criticalRepositoryNotFoundByDependencyLocatorErrorICityRepositoryEither ->
                criticalRepositoryNotFoundByDependencyLocatorErrorICityRepositoryEither.fold(
                    constantErrorReader, findAllWithRepository));
    }
}
