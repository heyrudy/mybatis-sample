package com.heyrudy.mybatissample.gateway.db.mock;

import com.heyrudy.mybatissample.domain.model.city.FullCity;
import com.heyrudy.mybatissample.domain.model.error.CriticalRepositoryNotFoundByLocatorError;
import com.heyrudy.mybatissample.domain.spi.ICityDbSPI;
import com.heyrudy.mybatissample.domain.spi.config.AppScopedLocator;
import cyclops.control.Reader;
import io.vavr.control.Either;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class MockedCityCriticalDbSPIAdapter implements ICityDbSPI {

    private final static Map<Long, FullCity> IN_MEMORY_DB = new ConcurrentHashMap<>();

    @Override
    public Reader<AppScopedLocator, Either<CriticalRepositoryNotFoundByLocatorError, FullCity>> save(
        FullCity fullCity) {
        return locator -> {
            IN_MEMORY_DB.put(fullCity.getId(), fullCity);
            return Either.right(IN_MEMORY_DB.get(fullCity.getId()));
        };
    }

    @Override
    public Reader<AppScopedLocator, Either<CriticalRepositoryNotFoundByLocatorError, List<FullCity>>> findCities() {
        return locator ->
            Either.right(IN_MEMORY_DB.values().stream().toList());
    }

    @Override
    public Reader<AppScopedLocator, Either<CriticalRepositoryNotFoundByLocatorError, Optional<FullCity>>> findCityById(
        long id) {
        return locator ->
            Either.right(Optional.ofNullable(IN_MEMORY_DB.get(id)));
    }
}
