package com.heyrudy.mybatissample.controller.config;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;

import com.heyrudy.mybatissample.domain.model.error.CriticalRepositoryNotFoundByLocatorError;
import com.heyrudy.mybatissample.domain.model.error.DbCriticalServiceNotFoundByLocatorError;
import com.heyrudy.mybatissample.domain.spi.config.AppScopedServiceLocator;
import com.heyrudy.mybatissample.domain.spi.config.ServiceKey;
import com.heyrudy.mybatissample.domain.spi.config.ServiceKey.CityDbSPIKey;
import com.heyrudy.mybatissample.domain.spi.config.ServiceKey.CriticalRepositoryKey;
import com.heyrudy.mybatissample.domain.spi.config.ServiceKey.DbCriticalServiceKey;
import com.heyrudy.mybatissample.gateway.db.mock.MockedCityCriticalDbSPIAdapter;
import io.vavr.control.Either;
import io.vavr.control.Option;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.context.ApplicationContext;

public class SpringAppScopedServiceLocator implements AppScopedServiceLocator {

    private final ApplicationContext applicationContext;

    public SpringAppScopedServiceLocator(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public <T> Either<CriticalRepositoryNotFoundByLocatorError, T> getCriticalRepository(
        CriticalRepositoryKey<T> key) {
        return getService(key)
            .toEither(new CriticalRepositoryNotFoundByLocatorError(
                ErrorMessage.NO_CRITICAL_REPOSITORY_FOUND_FOR_KEY_ERROR_MESSAGE
                    .formatted(key)));
    }

    @Override
    public <T> Either<DbCriticalServiceNotFoundByLocatorError, T> getDbCriticalService(
        DbCriticalServiceKey<T> key) {
        return getService(key)
            .toEither(new DbCriticalServiceNotFoundByLocatorError(
                ErrorMessage.NO_DB_SPI_CRITICAL_SERVICE_FOUND_FOR_KEY_ERROR_MESSAGE
                    .formatted(key)));
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
//            Map.entry(
//                CityRepositoryKey.INSTANCE,
//                getBeanOrMock(CityRepository.class, Option.none())),
            Map.entry(
                CityDbSPIKey.INSTANCE,
                new MockedCityCriticalDbSPIAdapter())
        );
    }

    private <T> T getBeanOrMock(Class<T> beanClass, Option<Supplier<T>> fallback) {
        return applicationContext.getBeanProvider(beanClass)
            .getIfAvailable(() -> fallback.map(Supplier::get).getOrNull());
    }
}
