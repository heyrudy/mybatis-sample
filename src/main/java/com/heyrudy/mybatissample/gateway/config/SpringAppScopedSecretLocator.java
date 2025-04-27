package com.heyrudy.mybatissample.gateway.config;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;

import com.heyrudy.mybatissample.domain.model.error.CriticalDbSecretPropertiesNotFoundBySecretLocatorError;
import com.heyrudy.mybatissample.domain.spi.config.CriticalDbSecretPropertiesKey;
import com.heyrudy.mybatissample.domain.spi.config.CriticalSecretKey;
import com.heyrudy.mybatissample.domain.spi.config.SecretKey;
import io.vavr.control.Either;
import io.vavr.control.Option;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.context.ApplicationContext;

public class SpringAppScopedSecretLocator implements AppScopedSecretLocator {

    private final ApplicationContext applicationContext;

    public SpringAppScopedSecretLocator(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public <T> Either<CriticalDbSecretPropertiesNotFoundBySecretLocatorError, T> getCriticalDbSecretProperties(
        CriticalSecretKey<T> key) {
        return getSecret(key)
            .toEither(new CriticalDbSecretPropertiesNotFoundBySecretLocatorError(
                AppScopedSecretLocator.ErrorMessage.NO_CRITICAL_DB_SECRET_PROPERTIES_FOUND_FOR_KEY_ERROR_MESSAGE
                    .formatted(key)));
    }

    @Override
    public <T> Option<T> getSecret(SecretKey<T> key) {
        return Option.of(secretMap().get(key))
            .filter(key.getType()::isInstance)
            .flatMap(it ->
                Match(it).option(
                    Case($(key.getType()::isInstance), key.getType()::cast)));
    }

    @Override
    public boolean hasSecret(SecretKey<?> key) {
        return secretMap().containsKey(key);
    }

    private Map<SecretKey<?>, ?> secretMap() {
        return Stream.of(dbSecretMap())
            .flatMap(it -> it.entrySet().stream())
            .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map<SecretKey<?>, ?> dbSecretMap() {
        return Map.ofEntries(
            Map.entry(
                CriticalDbSecretPropertiesKey.INSTANCE,
                getBeanOrMock(DbSecretProperties.class, Option.none()))
        );
    }

    private <T> T getBeanOrMock(Class<T> beanClass, Option<Supplier<T>> fallback) {
        return applicationContext.getBeanProvider(beanClass)
            .getIfAvailable(() -> fallback.map(Supplier::get).getOrNull());
    }
}
