package com.heyrudy.mybatissample.gateway.config;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;

import com.heyrudy.mybatissample.domain.model.error.CriticalDSLContextNotFoundByConfigLocatorError;
import com.heyrudy.mybatissample.domain.model.error.CriticalRepositoryNotFoundByServiceLocatorError;
import com.heyrudy.mybatissample.domain.model.error.DbCriticalServiceNotFoundByServiceLocatorError;
import com.heyrudy.mybatissample.domain.model.error.MissingCriticalConfigError;
import com.heyrudy.mybatissample.domain.spi.config.AppScopedServiceLocator;
import com.heyrudy.mybatissample.domain.spi.config.CityDbSPIKey;
import com.heyrudy.mybatissample.domain.spi.config.CriticalAppScopedConfigLocatorKey;
import com.heyrudy.mybatissample.domain.spi.config.CriticalConfigLocatorKey;
import com.heyrudy.mybatissample.domain.spi.config.CriticalRepositoryKey;
import com.heyrudy.mybatissample.domain.spi.config.DbCriticalServiceKey;
import com.heyrudy.mybatissample.domain.spi.config.ServiceKey;
import com.heyrudy.mybatissample.gateway.db.mock.MockedCityCriticalDbSPIAdapter;
import io.vavr.control.Either;
import io.vavr.control.Option;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestConfig {

    private final ApplicationContext applicationContext;

    TestConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    @Primary
    public AppScopedSecretLocator springTestAppScopedSecretLocator() {
        return new SpringAppScopedSecretLocator(applicationContext);
    }

    @Bean
    @Primary
    public AppScopedConfigLocator springTestAppScopedConfigLocator(
        AppScopedSecretLocator appScopedSecretLocator) {
        return new SpringAppScopedConfigLocator(appScopedSecretLocator);
    }

    @Bean
    @Primary
    public AppScopedServiceLocator springTestAppScopedServiceLocator(
        AppScopedConfigLocator appScopedConfigLocator) {
        return new SpringTestAppScopedServiceLocator(appScopedConfigLocator);
    }

    public static class SpringTestAppScopedServiceLocator implements AppScopedServiceLocator {

        private final AppScopedConfigLocator appScopedConfigLocator;

        public SpringTestAppScopedServiceLocator(AppScopedConfigLocator appScopedConfigLocator) {
            this.appScopedConfigLocator = appScopedConfigLocator;
        }

        @Override
        public <T> Either<MissingCriticalConfigError, T> getCriticalConfig(
            CriticalConfigLocatorKey<T> key) {
            return getService(key)
                .toEither(new CriticalDSLContextNotFoundByConfigLocatorError(
                    AppScopedServiceLocator.ErrorMessage.NO_CRITICAL_REPOSITORY_FOUND_FOR_KEY_ERROR_MESSAGE
                        .formatted(key)));
        }

        @Override
        public <T> Either<CriticalRepositoryNotFoundByServiceLocatorError, T> getCriticalRepository(
            CriticalRepositoryKey<T> key) {
            return getService(key)
                .toEither(new CriticalRepositoryNotFoundByServiceLocatorError(
                    AppScopedServiceLocator.ErrorMessage.NO_CRITICAL_REPOSITORY_FOUND_FOR_KEY_ERROR_MESSAGE
                        .formatted(key)));
        }

        @Override
        public <T> Either<DbCriticalServiceNotFoundByServiceLocatorError, T> getDbCriticalService(
            DbCriticalServiceKey<T> key) {
            return getService(key)
                .toEither(new DbCriticalServiceNotFoundByServiceLocatorError(
                    AppScopedServiceLocator.ErrorMessage.NO_DB_SPI_CRITICAL_SERVICE_FOUND_FOR_KEY_ERROR_MESSAGE
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
            return Stream.of(
                    configMap(),
                    cityServiceMap())
                .flatMap(it -> it.entrySet().stream())
                .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
        }

        private Map<ServiceKey<?>, ?> configMap() {
            return Map.ofEntries(
                Map.entry(
                    CriticalAppScopedConfigLocatorKey.INSTANCE,
                    appScopedConfigLocator)
            );
        }

        private Map<ServiceKey<?>, ?> cityServiceMap() {
            return Map.ofEntries(
//                Map.entry(
//                    CityRepositoryKey.INSTANCE,
//                    new MockedCityRepository()),
                Map.entry(
                    CityDbSPIKey.INSTANCE,
                    new MockedCityCriticalDbSPIAdapter())
            );
        }

//        private <T> T getBean(Class<T> beanClass) {
//            return applicationContext.getBeanProvider(beanClass).getObject();
//        }
    }
}