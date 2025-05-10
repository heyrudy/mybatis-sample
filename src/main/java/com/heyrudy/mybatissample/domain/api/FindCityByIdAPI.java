package com.heyrudy.mybatissample.domain.api;

import com.heyrudy.mybatissample.context.AppScopedDependencyLocator;
import com.heyrudy.mybatissample.context.MockedCityRepositoryKey;
import com.heyrudy.mybatissample.domain.model.city.ICity;
import com.heyrudy.mybatissample.domain.model.common.CityCriteriaDetails;
import com.heyrudy.mybatissample.domain.model.error.CityNotFoundByRepositoryError;
import com.heyrudy.mybatissample.domain.model.error.CityNotFoundError;
import com.heyrudy.mybatissample.domain.model.error.CityNotFoundError.ErrorMessage;
import com.heyrudy.mybatissample.domain.model.error.MissingCriticalDependencyError;
import com.heyrudy.mybatissample.domain.spi.ICityRepository;
import cyclops.control.Reader;
import io.vavr.control.Either;
import io.vavr.control.Option;
import java.util.Optional;
import java.util.function.Function;

public enum FindCityByIdAPI {
    INSTANCE;

    public Reader<AppScopedDependencyLocator, Either<CityNotFoundError, ICity>> execute(
        final CityCriteriaDetails cityCriteriaDetails) {
        // Define error mapping functions
        Function<MissingCriticalDependencyError, CityNotFoundError> mapDependencyError =
            missingCriticalDependencyError ->
                new CityNotFoundError(missingCriticalDependencyError.getMessage());
        Function<CityNotFoundByRepositoryError, CityNotFoundError> mapRepositoryError =
            cityNotFoundByRepositoryError ->
                new CityNotFoundError(cityNotFoundByRepositoryError.getMessage());
        // Create a "not found" error with the formatted message (precomputed)
        CityNotFoundError notFoundError = new CityNotFoundError(
            ErrorMessage.CITY_NOT_FOUND_ERROR_MESSAGE.formatted(cityCriteriaDetails.cityId()));
        // Function to handle Optional result and convert to Either
        Function<Optional<ICity>, Either<CityNotFoundError, ICity>> handleOptionalResult =
            iCityOpt -> Option.ofOptional(iCityOpt).toEither(notFoundError);
        // A reader that always returns a specific error value
        Function<CityNotFoundError, Reader<AppScopedDependencyLocator, Either<CityNotFoundError, ICity>>>
            constantErrorReader = cityNotFoundError ->
            __ -> Either.left(cityNotFoundError);
        // Function to convert a repository to a search operation
        Function<ICityRepository, Reader<AppScopedDependencyLocator, Either<CityNotFoundError, ICity>>>
            findWithRepository = iCityRepository ->
            iCityRepository.findById(cityCriteriaDetails.cityId())
                .map(cityNotFoundByRepositoryErrorOptionalEither ->
                    cityNotFoundByRepositoryErrorOptionalEither
                        .mapLeft(mapRepositoryError)
                        .flatMap(handleOptionalResult));
        // Compose operations with flatMap to explicitly avoid apply
        return MockedCityRepositoryKey.INSTANCE.describeDependencyContext()
            .map(iCityRepositoryEither ->
                iCityRepositoryEither.mapLeft(mapDependencyError))
            .flatMap(cityNotFoundErrorICityRepositoryEither ->
                cityNotFoundErrorICityRepositoryEither.fold(
                    constantErrorReader, findWithRepository));
    }
}
