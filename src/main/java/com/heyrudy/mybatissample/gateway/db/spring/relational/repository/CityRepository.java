package com.heyrudy.mybatissample.gateway.db.spring.relational.repository;

import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.table;

import com.heyrudy.mybatissample.domain.model.city.FullCity;
import com.heyrudy.mybatissample.domain.model.city.ICity;
import com.heyrudy.mybatissample.domain.model.error.CityNotFoundByRepositoryError;
import com.heyrudy.mybatissample.domain.model.error.CityNotSavedByRepositoryError;
import com.heyrudy.mybatissample.domain.model.error.CriticalDSLContextNotFoundByDependencyLocatorError;
import com.heyrudy.mybatissample.domain.spi.ICityRepository;
import com.heyrudy.mybatissample.context.AppScopedDependencyLocator;
import com.heyrudy.mybatissample.context.CriticalDSLContextKey;
import cyclops.control.Reader;
import io.vavr.control.Either;
import io.vavr.control.Option;
import java.util.List;
import java.util.Optional;
import org.jooq.Table;

public final class CityRepository implements ICityRepository {

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
        return appScopedDependencyLocator ->
            CriticalDSLContextKey.INSTANCE.describeDependencyContext()
                .apply(appScopedDependencyLocator)
                .mapLeft(missingCriticalDependencyError ->
                    new CityNotSavedByRepositoryError(missingCriticalDependencyError.getMessage()))
                .flatMap(dslContext ->
                    Option.of(dslContext.insertInto(CITIES)
                            .columns(NAME, STATE, COUNTRY)
                            .values(iCity.getName(), iCity.getState(), iCity.getCountry())
                            .returning()
                            .fetchOne())
                        .toEither(
                            new CityNotSavedByRepositoryError(
                                "Failed to insert city: No record returned"))
                        .map(CityRepository::mapRecord));
    }

    @Override
    public Reader<AppScopedDependencyLocator, Either<CriticalDSLContextNotFoundByDependencyLocatorError, List<ICity>>> findAll() {
        return appScopedDependencyLocator ->
            CriticalDSLContextKey.INSTANCE.describeDependencyContext()
                .apply(appScopedDependencyLocator)
                .mapLeft(missingCriticalDependencyError ->
                    new CriticalDSLContextNotFoundByDependencyLocatorError(
                        missingCriticalDependencyError.getMessage()))
                .map(dslContext ->
                    dslContext.select(ID, NAME, STATE, COUNTRY)
                        .from(CITIES)
                        .fetch()
                        .stream()
                        .map(CityRepository::mapRecord)
                        .toList());
    }

    @Override
    public Reader<AppScopedDependencyLocator, Either<CityNotFoundByRepositoryError, Optional<ICity>>> findById(
        long id) {
        return appScopedDependencyLocator ->
            CriticalDSLContextKey.INSTANCE.describeDependencyContext()
                .apply(appScopedDependencyLocator)
                .mapLeft(missingCriticalDependencyError ->
                    new CityNotFoundByRepositoryError(missingCriticalDependencyError.getMessage()))
                .map(dslContext ->
                    Option.of(dslContext.select(ID, NAME, STATE, COUNTRY)
                            .from(CITIES)
                            .where(ID.eq(id))
                            .fetchOne())
                        .toEither(
                            new CityNotFoundByRepositoryError(
                                "Failed to retrieve city with ID %d".formatted(id)))
                        .map(CityRepository::mapRecord)
                        .toOption()
                        .toJavaOptional());
    }
}
