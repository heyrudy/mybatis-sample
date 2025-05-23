package com.heyrudy.mybatissample.context;

import com.heyrudy.mybatissample.domain.error.CriticalDbSecretPropertiesNotFoundByDependencyLocatorError;
import com.heyrudy.mybatissample.domain.error.MissingCriticalDependencyError;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import cyclops.control.Reader;
import io.vavr.control.Either;
import io.vavr.control.Try;
import java.util.function.Function;

public enum CriticalDbSecretPropertiesKey
    implements CriticalSecretKey<IDbSecretProperties> {
    INSTANCE;

    private static final Function<Throwable, MissingCriticalDependencyError> CRITICAL_DB_SECRET_PROPERTIES_NOT_FOUND_BY_DEPENDENCY_LOCATOR_PATH =
        throwable ->
            new CriticalDbSecretPropertiesNotFoundByDependencyLocatorError(
                AppScopedDependencyLocator.ErrorMessage.NO_CRITICAL_DB_SECRET_PROPERTIES_FOUND_FOR_KEY_ERROR_MESSAGE
                    .formatted(INSTANCE, throwable.getMessage()));
    private static final Function<Config, IDbSecretProperties> GET_DB_SECRET_PROPERTIES_PATH =
        config ->
            DbSecretProperties.builder()
                .protocol(config.getString("db.secret.protocol"))
                .host(config.getString("db.secret.host"))
                .port(config.getInt("db.secret.port"))
                .schema(config.getString("db.secret.schema"))
                .username(config.getString("db.secret.username"))
                .password(config.getString("db.secret.password").toCharArray()).build();

    @Override
    public Reader<AppScopedDependencyLocator, Either<MissingCriticalDependencyError, IDbSecretProperties>> lazyLoad() {
        return __ ->
            Try.of(ConfigFactory::load)
                .toEither()
                .bimap(
                    CRITICAL_DB_SECRET_PROPERTIES_NOT_FOUND_BY_DEPENDENCY_LOCATOR_PATH,
                    GET_DB_SECRET_PROPERTIES_PATH);
    }

    @Override
    public String toString() {
        return "CriticalDatabasePropertiesKey{}";
    }
}