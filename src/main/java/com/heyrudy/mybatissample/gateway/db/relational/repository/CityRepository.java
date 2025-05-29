package com.heyrudy.mybatissample.gateway.db.relational.repository;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

import com.heyrudy.mybatissample.context.AppScopedDependencyLocator;
import com.heyrudy.mybatissample.context.CriticalH2DSLContextConfigKey;
import com.heyrudy.mybatissample.domain.error.CityNotFoundByRepositoryError;
import com.heyrudy.mybatissample.domain.error.CityNotSavedByRepositoryError;
import com.heyrudy.mybatissample.domain.error.CityTableNotTruncatedError;
import com.heyrudy.mybatissample.domain.error.CriticalDSLContextNotFoundByDependencyLocatorError;
import com.heyrudy.mybatissample.domain.error.DomainRepositoryError;
import com.heyrudy.mybatissample.domain.error.MissingCriticalDependencyError;
import com.heyrudy.mybatissample.domain.model.city.FullCity;
import com.heyrudy.mybatissample.domain.model.city.ICity;
import com.heyrudy.mybatissample.domain.spi.ICityRepository;
import cyclops.control.Reader;
import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Table;

public enum CityRepository
    implements ICityRepository {
    INSTANCE;

    // Define the table structure using jOOQ
    private static final Table<?> CITIES = table("city");
    private static final Field<Long> ID = field("id", Long.class);
    private static final Field<String> NAME = field("name", String.class);
    private static final Field<String> STATE = field("state", String.class);
    private static final Field<String> COUNTRY = field("country", String.class);

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

    private static final Reader<AppScopedDependencyLocator, Either<MissingCriticalDependencyError, DSLContext>> CRITICAL_DSL_CONTEXT_DEPENDENCY_LAZY_LOADED_PATH =
        CriticalH2DSLContextConfigKey.INSTANCE.lazyLoad();
    private static final Function<MissingCriticalDependencyError, Either<MissingCriticalDependencyError, List<ICity>>> CRITICAL_DSL_CONTEXT_NOT_FOUND_BY_DEPENDENCY_LOCATOR_PATH =
        missingCriticalDependencyError ->
            Either.left(
                new CriticalDSLContextNotFoundByDependencyLocatorError(
                    missingCriticalDependencyError.message()));
    private static final Function<MissingCriticalDependencyError, Either<DomainRepositoryError, ICity>> CITY_NOT_SAVED_BY_REPOSITORY_PATH =
        missingCriticalDependencyError ->
            Either.left(
                new CityNotSavedByRepositoryError(missingCriticalDependencyError.message()));
    private static final Function<MissingCriticalDependencyError, Either<DomainRepositoryError, Option<ICity>>> CITY_NOT_FOUND_BY_REPOSITORY_PATH =
        missingCriticalDependencyError ->
            Either.left(
                new CityNotFoundByRepositoryError(missingCriticalDependencyError.message()));
    private static final Function<MissingCriticalDependencyError, Either<DomainRepositoryError, Integer>> CITY_NOT_DELETED_BY_REPOSITORY_PATH =
        missingCriticalDependencyError ->
            Either.left(
                new CityNotFoundByRepositoryError(missingCriticalDependencyError.message()));
    // A reader that always returns a specific error value
    private static final Function<DSLContext, Either<MissingCriticalDependencyError, List<ICity>>> FIND_CITIES_PATH =
        dslContext ->
            Either.right(dslContext.select(ID, NAME, STATE, COUNTRY)
                .from(CITIES)
                .fetch()
                .stream()
                .map(CityRepository::mapRecord)
                .toList());
    private static final Function<ICity, Option<ICity>> TO_OPTIONAL_CITY_PATH = Option::of;
    private static final Function<ICity, Either<DomainRepositoryError, Option<ICity>>> TO_EITHER_CITY_PATH =
        TO_OPTIONAL_CITY_PATH.andThen(Either::right);

    @Override
    public Reader<AppScopedDependencyLocator, Either<DomainRepositoryError, ICity>> save(
        ICity iCity) {
        Function<DSLContext, Either<DomainRepositoryError, ICity>> saveCityPath =
            dslContext ->
                Option.of(dslContext.insertInto(CITIES)
                        .columns(NAME, STATE, COUNTRY)
                        .values(iCity.getName(), iCity.getState(), iCity.getCountry())
                        .returning(ID, NAME, STATE, COUNTRY)
                        .fetchOne())
                    .map(CityRepository::mapRecord)
                    .toEither(
                        new CityNotSavedByRepositoryError(
                            CityNotSavedByRepositoryError.ErrorMessage.CITY_NOT_SAVED));
        // Compose operations with flatMap to explicitly avoid apply
        return CRITICAL_DSL_CONTEXT_DEPENDENCY_LAZY_LOADED_PATH
            .map(dslContextEither ->
                dslContextEither.fold(CITY_NOT_SAVED_BY_REPOSITORY_PATH, saveCityPath));
    }

    @Override
    public Reader<AppScopedDependencyLocator, Either<MissingCriticalDependencyError, List<ICity>>> findAll() {
        // Compose operations with flatMap to explicitly avoid apply
        return CRITICAL_DSL_CONTEXT_DEPENDENCY_LAZY_LOADED_PATH
            .map(dslContextEither ->
                dslContextEither.fold(
                    CRITICAL_DSL_CONTEXT_NOT_FOUND_BY_DEPENDENCY_LOCATOR_PATH, FIND_CITIES_PATH));
    }

    @Override
    public Reader<AppScopedDependencyLocator, Either<DomainRepositoryError, Option<ICity>>> findById(
        long id) {
        Function<DSLContext, Either<DomainRepositoryError, Option<ICity>>> findCityByIdPath =
            dslContext ->
                Option.of(dslContext.select(ID, NAME, STATE, COUNTRY)
                        .from(CITIES)
                        .where(ID.eq(id))
                        .fetchOne())
                    .map(CityRepository::mapRecord)
                    .fold(
                        () ->
                            Either.left(
                                new CityNotFoundByRepositoryError(
                                    CityNotFoundByRepositoryError.ErrorMessage.CITY_NOT_FOUND_BY_ID
                                        .formatted(id))),
                        TO_EITHER_CITY_PATH);
        // Compose operations with flatMap to explicitly avoid apply
        return CRITICAL_DSL_CONTEXT_DEPENDENCY_LAZY_LOADED_PATH
            .map(dslContextEither ->
                dslContextEither.fold(CITY_NOT_FOUND_BY_REPOSITORY_PATH, findCityByIdPath));
    }

    public Reader<AppScopedDependencyLocator, Either<DomainRepositoryError, Integer>> emptyTable() {
        Function<DSLContext, Either<DomainRepositoryError, Integer>> deleteCityByIdPath =
            dslContext ->
                Try.of(() -> {
                        dslContext.truncate(CITIES).execute();
                        return dslContext.execute(
                            """
                                ALTER TABLE "public"."city" ALTER COLUMN "id" RESTART WITH 1
                                """
                        );
                    })
                    .toEither()
                    .bimap(
                        throwable ->
                            new CityTableNotTruncatedError(
                                CityTableNotTruncatedError.ErrorMessage.CITY_TABLE_NOT_TRUNCATED
                                    .formatted(throwable.getMessage())),
                        Function.identity());
        // Compose operations with flatMap to explicitly avoid apply
        return CRITICAL_DSL_CONTEXT_DEPENDENCY_LAZY_LOADED_PATH
            .map(dslContextEither ->
                dslContextEither.fold(CITY_NOT_DELETED_BY_REPOSITORY_PATH, deleteCityByIdPath));
    }
}
