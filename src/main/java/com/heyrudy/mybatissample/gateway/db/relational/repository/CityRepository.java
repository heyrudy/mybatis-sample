package com.heyrudy.mybatissample.gateway.db.relational.repository;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

import com.heyrudy.mybatissample.context.AppScopedDependencyLocator;
import com.heyrudy.mybatissample.context.CriticalDSLContextKey;
import com.heyrudy.mybatissample.domain.model.city.FullCity;
import com.heyrudy.mybatissample.domain.model.city.ICity;
import com.heyrudy.mybatissample.domain.model.error.CityNotFoundByRepositoryError;
import com.heyrudy.mybatissample.domain.model.error.CityNotSavedByRepositoryError;
import com.heyrudy.mybatissample.domain.model.error.CriticalDSLContextNotFoundByDependencyLocatorError;
import com.heyrudy.mybatissample.domain.model.error.MissingCriticalDependencyError;
import com.heyrudy.mybatissample.domain.spi.ICityRepository;
import cyclops.control.Reader;
import io.vavr.control.Either;
import io.vavr.control.Option;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import org.jooq.DSLContext;
import org.jooq.Table;

public enum CityRepository
    implements ICityRepository {
    INSTANCE;

    // Define the table structure using jOOQ
    private static final Table<?> CITIES = table("city");
    private static final org.jooq.Field<Long> ID = field("id", Long.class);
    private static final org.jooq.Field<String> NAME = field("name", String.class);
    private static final org.jooq.Field<String> STATE = field("state", String.class);
    private static final org.jooq.Field<String> COUNTRY = field("country", String.class);

    // Map jOOQ Record to our domain model
    private static ICity mapRecord(org.jooq.Record record) {
        return Optional.ofNullable(record)
            .map(it ->
                FullCity.builder()
                    .id(it.get(ID))
                    .name(it.get(NAME))
                    .state(it.get(STATE))
                    .country(it.get(COUNTRY)))
            .orElse(null);
    }

    private static final CriticalDSLContextKey CRITICAL_DSL_CONTEXT_KEY = CriticalDSLContextKey.INSTANCE;
    // Define error mapping function
    private static final Function<MissingCriticalDependencyError, CityNotSavedByRepositoryError> MISSING_CRITICAL_DEPENDENCY_ERROR_CITY_NOT_SAVED_BY_REPOSITORY_ERROR =
        missingCriticalDependencyError ->
            new CityNotSavedByRepositoryError(missingCriticalDependencyError.getMessage());
    private static final Function<MissingCriticalDependencyError, CriticalDSLContextNotFoundByDependencyLocatorError> MISSING_CRITICAL_DEPENDENCY_ERROR_CRITICAL_DSL_CONTEXT_NOT_FOUND_BY_DEPENDENCY_LOCATOR_ERROR =
        missingCriticalDependencyError ->
            new CriticalDSLContextNotFoundByDependencyLocatorError(
                missingCriticalDependencyError.getMessage());
    private static final Function<MissingCriticalDependencyError, CityNotFoundByRepositoryError> MISSING_CRITICAL_DEPENDENCY_ERROR_CITY_NOT_FOUND_BY_REPOSITORY_ERROR =
        missingCriticalDependencyError ->
            new CityNotFoundByRepositoryError(missingCriticalDependencyError.getMessage());
    // A reader that always returns a specific error value
    private static final Function<CityNotFoundByRepositoryError, Reader<AppScopedDependencyLocator, Either<CityNotFoundByRepositoryError, Option<ICity>>>> CONSTANT_CITY_NOT_FOUND_BY_REPOSITORY_ERROR_READER =
        cityNotFoundByRepositoryError ->
            __ -> Either.left(cityNotFoundByRepositoryError);
    private static final Function<CityNotSavedByRepositoryError, Reader<AppScopedDependencyLocator, Either<CityNotSavedByRepositoryError, ICity>>> CONSTANT_CITY_NOT_SAVED_BY_REPOSITORY_ERROR_READER =
        cityNotSavedByRepositoryError ->
            __ -> Either.left(cityNotSavedByRepositoryError);
    private static final Function<CriticalDSLContextNotFoundByDependencyLocatorError, Reader<AppScopedDependencyLocator, Either<CriticalDSLContextNotFoundByDependencyLocatorError, List<ICity>>>> CONSTANT_CRITICAL_DSL_CONTEXT_NOT_FOUND_BY_DEPENDENCY_LOCATOR_ERROR_READER =
        criticalDSLContextNotFoundByDependencyLocatorError ->
            __ -> Either.left(criticalDSLContextNotFoundByDependencyLocatorError);

    @Override
    public Reader<AppScopedDependencyLocator, Either<CityNotSavedByRepositoryError, ICity>> save(
        ICity iCity) {
        // Function that transforms a DSLContext into a Reader that produces the city insertion result
        // Perform the insert operation
        // Convert the result to Either
        Function<DSLContext, Reader<AppScopedDependencyLocator, Either<CityNotSavedByRepositoryError, ICity>>> dslContextToReader =
            dslContext ->
                __ -> Option.of(dslContext.insertInto(CITIES)
                        .columns(NAME, STATE, COUNTRY)
                        .values(iCity.getName(), iCity.getState(), iCity.getCountry())
                        .returning()
                        .fetchOne())
                    .toEither(new CityNotSavedByRepositoryError(
                        "Failed to insert city: No record returned"))
                    .map(CityRepository::mapRecord);
        // Compose operations with flatMap to explicitly avoid apply
        return CRITICAL_DSL_CONTEXT_KEY.describeDependencyContext()
            .map(dslContextEither ->
                dslContextEither.mapLeft(
                    MISSING_CRITICAL_DEPENDENCY_ERROR_CITY_NOT_SAVED_BY_REPOSITORY_ERROR))
            .flatMap(cityNotSavedByRepositoryErrorDSLContextEither ->
                cityNotSavedByRepositoryErrorDSLContextEither.fold(
                    CONSTANT_CITY_NOT_SAVED_BY_REPOSITORY_ERROR_READER, dslContextToReader));
    }

    @Override
    public Reader<AppScopedDependencyLocator, Either<CriticalDSLContextNotFoundByDependencyLocatorError, List<ICity>>> findAll() {
        // Function that transforms a DSLContext into a Reader that produces the cities lookup result
        // Perform the findAll operation
        // Convert the result to Either
        Function<DSLContext, Reader<AppScopedDependencyLocator, Either<CriticalDSLContextNotFoundByDependencyLocatorError, List<ICity>>>> dslContextToReader =
            dslContext ->
                __ -> Either.right(dslContext.select(ID, NAME, STATE, COUNTRY)
                    .from(CITIES)
                    .fetch()
                    .stream()
                    .map(CityRepository::mapRecord)
                    .toList());
        // Compose operations with flatMap to explicitly avoid apply
        return CRITICAL_DSL_CONTEXT_KEY.describeDependencyContext()
            .map(dslContextEither ->
                dslContextEither.mapLeft(
                    MISSING_CRITICAL_DEPENDENCY_ERROR_CRITICAL_DSL_CONTEXT_NOT_FOUND_BY_DEPENDENCY_LOCATOR_ERROR))
            .flatMap(criticalDSLContextNotFoundByDependencyLocatorErrorDSLContextEither ->
                criticalDSLContextNotFoundByDependencyLocatorErrorDSLContextEither.fold(
                    CONSTANT_CRITICAL_DSL_CONTEXT_NOT_FOUND_BY_DEPENDENCY_LOCATOR_ERROR_READER,
                    dslContextToReader));
    }

    @Override
    public Reader<AppScopedDependencyLocator, Either<CityNotFoundByRepositoryError, Option<ICity>>> findById(
        long id) {
        // Function that transforms a DSLContext into a Reader that produces the cities lookup result
        // Perform the findAll operation
        // Convert the result to Either
        Function<DSLContext, Reader<AppScopedDependencyLocator, Either<CityNotFoundByRepositoryError, Option<ICity>>>> dslContextToReader =
            dslContext ->
                __ -> Option.of(dslContext.select(ID, NAME, STATE, COUNTRY)
                        .from(CITIES)
                        .where(ID.eq(id))
                        .fetchOne())
                    .map(CityRepository::mapRecord)
                    .fold(
                        () -> Either.left(new CityNotFoundByRepositoryError(
                            "Failed to retrieve city with ID %d".formatted(id))),
                        iCity -> Either.right(Option.of(iCity)));
        // Compose operations with flatMap to explicitly avoid apply
        return CRITICAL_DSL_CONTEXT_KEY.describeDependencyContext()
            .map(dslContextEither ->
                dslContextEither.mapLeft(
                    MISSING_CRITICAL_DEPENDENCY_ERROR_CITY_NOT_FOUND_BY_REPOSITORY_ERROR))
            .flatMap(cityNotFoundByRepositoryErrorDSLContextEither ->
                cityNotFoundByRepositoryErrorDSLContextEither.fold(
                    CONSTANT_CITY_NOT_FOUND_BY_REPOSITORY_ERROR_READER, dslContextToReader));
    }
}
