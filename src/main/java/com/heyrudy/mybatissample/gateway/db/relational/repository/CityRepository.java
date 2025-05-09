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

    @Override
    public Reader<AppScopedDependencyLocator, Either<CityNotSavedByRepositoryError, ICity>> save(
        ICity iCity) {
        // Define error mapping function
        Function<MissingCriticalDependencyError, CityNotSavedByRepositoryError> mapDependencyError =
            missingCriticalDependencyError ->
                new CityNotSavedByRepositoryError(missingCriticalDependencyError.getMessage());
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
        // A reader that always returns a specific error value
        Function<CityNotSavedByRepositoryError, Reader<AppScopedDependencyLocator, Either<CityNotSavedByRepositoryError, ICity>>>
            constantErrorReader = cityNotSavedByRepositoryError ->
            __ -> Either.left(cityNotSavedByRepositoryError);
        // Compose operations with flatMap to explicitly avoid apply
        return CriticalDSLContextKey.INSTANCE.describeDependencyContext()
            .map(dslContextEither ->
                dslContextEither.mapLeft(mapDependencyError))
            .flatMap(cityNotSavedByRepositoryErrorDSLContextEither ->
                cityNotSavedByRepositoryErrorDSLContextEither.fold(
                    constantErrorReader, dslContextToReader));
    }

    @Override
    public Reader<AppScopedDependencyLocator, Either<CriticalDSLContextNotFoundByDependencyLocatorError, List<ICity>>> findAll() {
        // Define error mapping function
        Function<MissingCriticalDependencyError, CriticalDSLContextNotFoundByDependencyLocatorError> mapDependencyError =
            missingCriticalDependencyError ->
                new CriticalDSLContextNotFoundByDependencyLocatorError(
                    missingCriticalDependencyError.getMessage());
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
        // A reader that always returns a specific error value
        Function<CriticalDSLContextNotFoundByDependencyLocatorError, Reader<AppScopedDependencyLocator, Either<CriticalDSLContextNotFoundByDependencyLocatorError, List<ICity>>>>
            constantErrorReader = criticalDSLContextNotFoundByDependencyLocatorError ->
            __ -> Either.left(criticalDSLContextNotFoundByDependencyLocatorError);
        // Compose operations with flatMap to explicitly avoid apply
        return CriticalDSLContextKey.INSTANCE.describeDependencyContext()
            .map(dslContextEither ->
                dslContextEither.mapLeft(mapDependencyError))
            .flatMap(criticalDSLContextNotFoundByDependencyLocatorErrorDSLContextEither ->
                criticalDSLContextNotFoundByDependencyLocatorErrorDSLContextEither.fold(
                    constantErrorReader, dslContextToReader));
    }

    @Override
    public Reader<AppScopedDependencyLocator, Either<CityNotFoundByRepositoryError, Optional<ICity>>> findById(
        long id) {
        Function<MissingCriticalDependencyError, CityNotFoundByRepositoryError> mapDependencyError =
            missingCriticalDependencyError ->
                new CityNotFoundByRepositoryError(missingCriticalDependencyError.getMessage());
        // Function that transforms a DSLContext into a Reader that produces the cities lookup result
        // Perform the findAll operation
        // Convert the result to Either
        Function<DSLContext, Reader<AppScopedDependencyLocator, Either<CityNotFoundByRepositoryError, Optional<ICity>>>> dslContextToReader =
            dslContext ->
                __ -> Option.of(dslContext.select(ID, NAME, STATE, COUNTRY)
                        .from(CITIES)
                        .where(ID.eq(id))
                        .fetchOne())
                    .map(CityRepository::mapRecord)
                    .fold(
                        () -> Either.left(new CityNotFoundByRepositoryError(
                            "Failed to retrieve city with ID %d".formatted(id))),
                        iCity -> Either.right(Optional.of(iCity)));
        // A reader that always returns a specific error value
        Function<CityNotFoundByRepositoryError, Reader<AppScopedDependencyLocator, Either<CityNotFoundByRepositoryError, Optional<ICity>>>>
            constantErrorReader = cityNotFoundByRepositoryError ->
            __ -> Either.left(cityNotFoundByRepositoryError);
        // Compose operations with flatMap to explicitly avoid apply
        return CriticalDSLContextKey.INSTANCE.describeDependencyContext()
            .map(dslContextEither ->
                dslContextEither.mapLeft(mapDependencyError))
            .flatMap(cityNotFoundByRepositoryErrorDSLContextEither ->
                cityNotFoundByRepositoryErrorDSLContextEither.fold(
                    constantErrorReader, dslContextToReader));
    }
}
