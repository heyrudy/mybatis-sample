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
    private static final Function<MissingCriticalDependencyError, Either<CityNotSavedByRepositoryError, ICity>> CITY_NOT_SAVED_BY_REPOSITORY_PATH =
        missingCriticalDependencyError ->
            Either.left(
                new CityNotSavedByRepositoryError(missingCriticalDependencyError.getMessage()));
    private static final Function<MissingCriticalDependencyError, Either<CriticalDSLContextNotFoundByDependencyLocatorError, List<ICity>>> CRITICAL_DSL_CONTEXT_NOT_FOUND_BY_DEPENDENCY_LOCATOR_PATH =
        missingCriticalDependencyError ->
            Either.left(new CriticalDSLContextNotFoundByDependencyLocatorError(
                missingCriticalDependencyError.getMessage()));
    private static final Function<MissingCriticalDependencyError, Either<CityNotFoundByRepositoryError, Option<ICity>>> CITY_NOT_FOUND_BY_REPOSITORY_PATH =
        missingCriticalDependencyError ->
            Either.left(
                new CityNotFoundByRepositoryError(missingCriticalDependencyError.getMessage()));
    // A reader that always returns a specific error value
    private static final Function<DSLContext, Either<CriticalDSLContextNotFoundByDependencyLocatorError, List<ICity>>> FIND_CITIES_PATH =
        dslContext ->
            Either.right(dslContext.select(ID, NAME, STATE, COUNTRY)
                .from(CITIES)
                .fetch()
                .stream()
                .map(CityRepository::mapRecord)
                .toList());
    private static final Function<ICity, Option<ICity>> TO_OPTIONAL_CITY_PATH = Option::of;
    private static final Function<ICity, Either<CityNotFoundByRepositoryError, Option<ICity>>> TO_EITHER_CITY_PATH =
        TO_OPTIONAL_CITY_PATH.andThen(Either::right);

    @Override
    public Reader<AppScopedDependencyLocator, Either<CityNotSavedByRepositoryError, ICity>> save(
        ICity iCity) {
        Function<DSLContext, Either<CityNotSavedByRepositoryError, ICity>> saveCityPath =
            dslContext ->
                Option.of(dslContext.insertInto(CITIES)
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
                dslContextEither.fold(CITY_NOT_SAVED_BY_REPOSITORY_PATH, saveCityPath));
    }

    @Override
    public Reader<AppScopedDependencyLocator, Either<CriticalDSLContextNotFoundByDependencyLocatorError, List<ICity>>> findAll() {
        // Compose operations with flatMap to explicitly avoid apply
        return CRITICAL_DSL_CONTEXT_KEY.describeDependencyContext()
            .map(dslContextEither ->
                dslContextEither.fold(
                    CRITICAL_DSL_CONTEXT_NOT_FOUND_BY_DEPENDENCY_LOCATOR_PATH, FIND_CITIES_PATH));
    }

    @Override
    public Reader<AppScopedDependencyLocator, Either<CityNotFoundByRepositoryError, Option<ICity>>> findById(
        long id) {
        Function<DSLContext, Either<CityNotFoundByRepositoryError, Option<ICity>>> findCityByIdPath =
            dslContext ->
                Option.of(dslContext.select(ID, NAME, STATE, COUNTRY)
                        .from(CITIES)
                        .where(ID.eq(id))
                        .fetchOne())
                    .map(CityRepository::mapRecord)
                    .fold(
                        () -> Either.left(new CityNotFoundByRepositoryError(
                            "Failed to retrieve city with ID %d".formatted(id))),
                        TO_EITHER_CITY_PATH);
        // Compose operations with flatMap to explicitly avoid apply
        return CRITICAL_DSL_CONTEXT_KEY.describeDependencyContext()
            .map(dslContextEither ->
                dslContextEither.fold(CITY_NOT_FOUND_BY_REPOSITORY_PATH, findCityByIdPath));
    }
}
