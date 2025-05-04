package com.heyrudy.mybatissample.gateway.config;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;

import com.heyrudy.mybatissample.controller.config.SpringAppScopedDependencyLocator;
import com.heyrudy.mybatissample.domain.model.error.MissingCriticalDependencyError;
import com.heyrudy.mybatissample.domain.spi.config.AppScopedDependencyLocator;
import com.heyrudy.mybatissample.domain.spi.config.CityRepositoryKey;
import com.heyrudy.mybatissample.domain.spi.config.DependencyKey;
import com.heyrudy.mybatissample.domain.spi.config.EnvironmentKey;
import com.heyrudy.mybatissample.gateway.db.spring.relational.repository.MockedCityRepository;
import io.vavr.control.Either;
import java.util.Map;
import java.util.function.Function;
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
    public AppScopedDependencyLocator springTestAppScopedDependencyLocator() {
        return new SpringAppScopedDependencyLocator(applicationContext);
    }

    public static class SpringTestAppScopedDependencyLocator implements AppScopedDependencyLocator {

        @Override
        public <T> Either<MissingCriticalDependencyError, T> getDependency(DependencyKey<T> key) {
            return Match(dependencyMap().get(key)).of(
                Case($(key.getType()::isInstance),
                    v ->
                        Either.right(key.getType().cast(v))),
                Case($(),
                    () ->
                        ErrorMessage.toDependencyError(key)
                            .map(Either::<MissingCriticalDependencyError, T>left)
                            .fold(
                                () -> Either.left(new MissingCriticalDependencyError(
                                    "Unknown error for key: %s".formatted(key))),
                                Function.identity()
                            ))
            );
        }

        private Map<EnvironmentKey<?>, ?> dependencyMap() {
            return Map.ofEntries(
                Map.entry(
                    CityRepositoryKey.INSTANCE, new MockedCityRepository())
            );
        }

//        private <T> T getBean(Class<T> beanClass) {
//            return applicationContext.getBeanProvider(beanClass).getObject();
//        }
    }
}