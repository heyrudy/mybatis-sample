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
import java.util.function.Function;

public enum FindCityByIdAPI {
    INSTANCE;

    // Define error mapping functions
    public static final Function<MissingCriticalDependencyError, CityNotFoundError> MAP_DEPENDENCY_ERROR = missingCriticalDependencyError ->
        new CityNotFoundError(missingCriticalDependencyError.getMessage());
    public static final Function<CityNotFoundByRepositoryError, CityNotFoundError> MAP_REPOSITORY_ERROR = cityNotFoundByRepositoryError ->
        new CityNotFoundError(cityNotFoundByRepositoryError.getMessage());
    // A reader that always returns a specific error value
    public static final Function<CityNotFoundError, Reader<AppScopedDependencyLocator, Either<CityNotFoundError, ICity>>> CONSTANT_CITY_NOY_FOUND_ERROR_READER = cityNotFoundError ->
        __ -> Either.left(cityNotFoundError);

    public Reader<AppScopedDependencyLocator, Either<CityNotFoundError, ICity>> execute(
        final CityCriteriaDetails cityCriteriaDetails) {
        // Create a "not found" error with the formatted message (precomputed)
        CityNotFoundError notFoundError = new CityNotFoundError(
            ErrorMessage.CITY_NOT_FOUND_ERROR_MESSAGE.formatted(cityCriteriaDetails.cityId()));
        // Function to handle Optional result and convert to Either
        Function<ICityRepository, Reader<AppScopedDependencyLocator, Either<CityNotFoundError, ICity>>> findWithRepository =
            iCityRepository ->
                iCityRepository.findById(cityCriteriaDetails.cityId())
                    .map(cityNotFoundByRepositoryErrorOptionalEither ->
                        cityNotFoundByRepositoryErrorOptionalEither
                            .mapLeft(MAP_REPOSITORY_ERROR)
                            .flatMap(iCityOpt ->
                                Option.ofOptional(iCityOpt).toEither(notFoundError)));
        // Compose operations with flatMap to explicitly avoid apply
        return MockedCityRepositoryKey.INSTANCE.describeDependencyContext()
            .map(iCityRepositoryEither ->
                iCityRepositoryEither.mapLeft(MAP_DEPENDENCY_ERROR))
            .flatMap(cityNotFoundErrorICityRepositoryEither ->
                cityNotFoundErrorICityRepositoryEither.fold(
                    CONSTANT_CITY_NOY_FOUND_ERROR_READER, findWithRepository));
    }
}
