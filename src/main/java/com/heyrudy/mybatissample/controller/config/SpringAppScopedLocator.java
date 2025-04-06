package com.heyrudy.mybatissample.controller.config;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;

import com.heyrudy.mybatissample.domain.spi.AppScopedLocator;
import com.heyrudy.mybatissample.domain.spi.ServiceKey;
import com.heyrudy.mybatissample.domain.spi.ServiceKey.CityDbKey;
import com.heyrudy.mybatissample.domain.spi.ServiceKey.DbServiceKey;
import com.heyrudy.mybatissample.domain.model.error.DBServiceNotFoundByLocatorError;
import com.heyrudy.mybatissample.domain.spi.ICityDbSPI;
import com.heyrudy.mybatissample.gateway.db.mock.MockedCityDbAdapter;
import io.vavr.control.Either;
import io.vavr.control.Option;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.context.ApplicationContext;

public class SpringAppScopedLocator implements AppScopedLocator {

    private final ApplicationContext applicationContext;

    public SpringAppScopedLocator(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public <T> Either<DBServiceNotFoundByLocatorError, T> getDBService(DbServiceKey<T> key) {
        return getService(key)
            .toEither(new DBServiceNotFoundByLocatorError(
                "No DB service found for key: %s".formatted(key)));
    }

    @Override
    public <T> Option<T> getService(ServiceKey<T> key) {
        return Option.of(serviceMap().get(key))
            .filter(key.getType()::isInstance)
            .flatMap(it ->
                Match(it).option(
                    Case($(key.getType()::isInstance), key.getType()::cast)));
    }

    @Override
    public boolean hasService(ServiceKey<?> key) {
        return serviceMap().containsKey(key);
    }

    private Map<ServiceKey<?>, ?> serviceMap() {
        return Stream.of(cityServiceMap())
            .flatMap(it -> it.entrySet().stream())
            .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map<ServiceKey<?>, ?> cityServiceMap() {
        return Map.ofEntries(
            Map.entry(
                CityDbKey.INSTANCE, getBeanOrMock(ICityDbSPI.class, MockedCityDbAdapter::new))
        );
    }

    private <T> T getBeanOrMock(Class<T> beanClass, Supplier<T> fallback) {
        return applicationContext.getBeanProvider(beanClass)
            .getIfAvailable(fallback);
    }
}
