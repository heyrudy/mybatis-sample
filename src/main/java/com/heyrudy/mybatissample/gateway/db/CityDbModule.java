package com.heyrudy.mybatissample.gateway.db;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

import com.heyrudy.mybatissample.application.context.AppScopedDependencyLocator;
import com.heyrudy.mybatissample.application.context.CriticalConfigKey.CriticalH2DSLContextConfigKey;
import com.heyrudy.mybatissample.domain.CityModelModule;
import com.heyrudy.mybatissample.domain.CityModelModule.FullCity.FullCityMutatorOptions;
import com.heyrudy.mybatissample.domain.CityRepositoryError;
import com.heyrudy.mybatissample.domain.DomainErrorModule;
import com.heyrudy.mybatissample.domain.DomainRepositoryError;
import com.heyrudy.mybatissample.domain.MissingCriticalConfigError.CriticalDSLContextNotFoundByDependencyLocatorError;
import cyclops.control.Reader;
import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Try;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Table;

public interface CityDbModule
    extends CityModelModule,
    DomainErrorModule {

    interface ICityRepository {

        /**
         * Saves a city.
         *
         * @param iCity The city to save
         * @return A Reader monad that either results in an error or the saved city
         */
        Reader<AppScopedDependencyLocator, Either<DomainRepositoryError, ICity>> save(ICity iCity);

        /**
         * Finds all cities.
         *
         * @return A Reader monad that either results in an error or a list of cities
         */
        Reader<AppScopedDependencyLocator, Either<DomainErrorModule.MissingCriticalDependencyError, List<ICity>>> findAll();

        /**
         * Finds a city by its ID.
         *
         * @param id The ID of the city to find
         * @return A Reader monad that either results in an error or an optional city
         */
        Reader<AppScopedDependencyLocator, Either<DomainRepositoryError, Option<ICity>>> findById(
            long id);
    }

    enum MockedCityRepository
        implements ICityRepository {
        INSTANCE;

        private final static Map<Long, ICity> IN_MEMORY_DB = new ConcurrentHashMap<>();

        @Override
        public Reader<AppScopedDependencyLocator, Either<DomainRepositoryError, ICity>> save(
            ICity iCity) {
            return __ -> {
                Function<Map<Long, ICity>, Long> idGenerator = AutoIncrementMap.atomicGenerator();
                Long newCityId = AutoIncrementMap.putWithAutoIncrement(
                    IN_MEMORY_DB, null, iCity, idGenerator);
                iCity.setId(newCityId);
                return Either.right(IN_MEMORY_DB.get(newCityId));
            };
        }

        @Override
        public Reader<AppScopedDependencyLocator, Either<DomainErrorModule.MissingCriticalDependencyError, List<ICity>>> findAll() {
            return __ ->
                Either.right(IN_MEMORY_DB.values().stream().toList());
        }

        @Override
        public Reader<AppScopedDependencyLocator, Either<DomainRepositoryError, Option<ICity>>> findById(
            long id) {
            return __ ->
                Either.right(Option.of(IN_MEMORY_DB.get(id)));
        }

        /**
         * A functional utility for working with maps that auto-increment keys
         */
        public static class AutoIncrementMap {

            /**
             * Adds a value to a map with an auto-incremented key if no key is provided
             *
             * @param map The map to add to
             * @param keyOrNull The key (or null to auto-generate)
             * @param value The value to add
             * @param nextKeyGenerator The key type (must be comparable/incrementable)
             * @param <V> The value type
             * @return The key that was used (either provided or generated)
             */
            public static <V> Long putWithAutoIncrement(
                Map<Long, V> map,
                Long keyOrNull,
                V value,
                Function<Map<Long, V>, Long> nextKeyGenerator) {
                // If a key is provided, use it directly
                if (keyOrNull != null) {
                    map.put(keyOrNull, value);
                    return keyOrNull;
                }

                // Otherwise, generate the next key and use it
                Long newKey = nextKeyGenerator.apply(map);
                map.put(newKey, value);
                return newKey;
            }

            /**
             * Creates a simple next key generator that finds the max key and adds 1
             */
            public static <V> Function<Map<Long, V>, Long> maxPlusOneGenerator() {
                return map -> map.keySet().stream()
                    .mapToLong(Long::longValue)
                    .max()
                    .orElse(0L) + 1L;
            }

            /**
             * Creates an efficient next key generator using AtomicLong
             */
            public static <V> Function<Map<Long, V>, Long> atomicGenerator() {
                // Start from 1 like typical DB auto-increment
                AtomicLong sequence = new AtomicLong(1);

                return map -> {
                    // If a map is empty, reset to 1
                    if (map.isEmpty()) {
                        sequence.set(1);
                        return 1L;
                    }

                    // Find the highest key to ensure we're always higher
                    long maxKey = map.keySet().stream()
                        .mapToLong(Long::longValue)
                        .max()
                        .orElse(0L);

                    // Update a sequence if the current max is higher than our counter
                    if (maxKey >= sequence.get()) {
                        sequence.set(maxKey + 1);
                    }

                    return sequence.getAndIncrement();
                };
            }
        }
    }

    enum CityRepository
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
                    FullCity.with(
                        FullCityMutatorOptions.INSTANCE.id(it.get(ID)),
                        FullCityMutatorOptions.INSTANCE.name(it.get(NAME)),
                        FullCityMutatorOptions.INSTANCE.state(it.get(STATE)),
                        FullCityMutatorOptions.INSTANCE.country(it.get(COUNTRY))))
                .orElse(null);
        }

        private static final Reader<AppScopedDependencyLocator, Either<DomainErrorModule.MissingCriticalDependencyError, DSLContext>> CRITICAL_DSL_CONTEXT_DEPENDENCY_LAZY_LOADED_PATH =
            CriticalH2DSLContextConfigKey.INSTANCE.lazyLoad();
        private static final Function<DomainErrorModule.MissingCriticalDependencyError, String> MISSING_CRITICAL_DEPENDENCY_ERROR_MESSAGE =
            DomainErrorModule.MissingCriticalDependencyError::message;
        private static final Function<DomainErrorModule.MissingCriticalDependencyError, Either<DomainErrorModule.MissingCriticalDependencyError, List<ICity>>> CRITICAL_DSL_CONTEXT_NOT_FOUND_BY_DEPENDENCY_LOCATOR_PATH =
            MISSING_CRITICAL_DEPENDENCY_ERROR_MESSAGE
                .andThen(CriticalDSLContextNotFoundByDependencyLocatorError::new)
                .andThen(Either::left);
        private static final Function<DomainErrorModule.MissingCriticalDependencyError, Either<DomainRepositoryError, ICity>> CITY_NOT_SAVED_BY_REPOSITORY_PATH =
            MISSING_CRITICAL_DEPENDENCY_ERROR_MESSAGE
                .andThen(CityRepositoryError.CityNotSavedByRepositoryError::new)
                .andThen(Either::left);
        private static final Function<DomainErrorModule.MissingCriticalDependencyError, Either<DomainRepositoryError, Option<ICity>>> CITY_NOT_FOUND_BY_REPOSITORY_PATH =
            MISSING_CRITICAL_DEPENDENCY_ERROR_MESSAGE
                .andThen(CityRepositoryError.CityNotFoundByRepositoryError::new)
                .andThen(Either::left);
        private static final Function<DomainErrorModule.MissingCriticalDependencyError, Either<DomainRepositoryError, Integer>> CITY_NOT_DELETED_BY_REPOSITORY_PATH =
            MISSING_CRITICAL_DEPENDENCY_ERROR_MESSAGE
                .andThen(CityRepositoryError.CityNotFoundByRepositoryError::new)
                .andThen(Either::left);
        private static final Function<DSLContext, Either<DomainErrorModule.MissingCriticalDependencyError, List<ICity>>> FIND_CITIES_PATH =
            dslContext ->
                Either.right(dslContext.select(ID, NAME, STATE, COUNTRY)
                    .from(CITIES)
                    .fetch()
                    .stream()
                    .map(CityRepository::mapRecord)
                    .toList());
        private static final Function<ICity, Option<ICity>> TO_OPTIONAL_CITY_PATH =
            Option::of;
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
                            new CityRepositoryError.CityNotSavedByRepositoryError(
                                CityRepositoryError.CityNotSavedByRepositoryError.CityNotSavedByRepositoryError.ErrorMessage.CITY_NOT_SAVED));
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
                        CRITICAL_DSL_CONTEXT_NOT_FOUND_BY_DEPENDENCY_LOCATOR_PATH,
                        FIND_CITIES_PATH));
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
                                    new CityRepositoryError.CityNotFoundByRepositoryError(
                                        CityRepositoryError.CityNotFoundByRepositoryError.CityNotFoundByRepositoryError.ErrorMessage.CITY_NOT_FOUND_BY_ID
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
                                new CityRepositoryError.CityTableNotTruncatedError(
                                    CityRepositoryError.CityTableNotTruncatedError.CityTableNotTruncatedError.ErrorMessage.CITY_TABLE_NOT_TRUNCATED
                                        .formatted(throwable.getMessage())),
                            Function.identity());
            // Compose operations with flatMap to explicitly avoid apply
            return CRITICAL_DSL_CONTEXT_DEPENDENCY_LAZY_LOADED_PATH
                .map(dslContextEither ->
                    dslContextEither.fold(CITY_NOT_DELETED_BY_REPOSITORY_PATH, deleteCityByIdPath));
        }
    }
}
