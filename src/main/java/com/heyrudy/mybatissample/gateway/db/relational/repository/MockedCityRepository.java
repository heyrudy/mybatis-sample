package com.heyrudy.mybatissample.gateway.db.relational.repository;

import com.heyrudy.mybatissample.context.AppScopedDependencyLocator;
import com.heyrudy.mybatissample.domain.model.city.ICity;
import com.heyrudy.mybatissample.domain.model.error.DomainRepositoryError;
import com.heyrudy.mybatissample.domain.model.error.MissingCriticalDependencyError;
import com.heyrudy.mybatissample.domain.spi.ICityRepository;
import cyclops.control.Reader;
import io.vavr.control.Either;
import io.vavr.control.Option;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

public enum MockedCityRepository
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
    public Reader<AppScopedDependencyLocator, Either<MissingCriticalDependencyError, List<ICity>>> findAll() {
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
            AtomicLong sequence = new AtomicLong(1); // Start from 1 like typical DB auto-increment

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
