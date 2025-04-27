package com.heyrudy.mybatissample.gateway.config;

import com.heyrudy.mybatissample.domain.model.error.CriticalDbSecretPropertiesNotFoundBySecretLocatorError;
import com.heyrudy.mybatissample.domain.spi.config.CriticalSecretKey;
import com.heyrudy.mybatissample.domain.spi.config.SecretKey;
import io.vavr.control.Either;
import io.vavr.control.Option;

public interface AppScopedSecretLocator {

    <T> Either<CriticalDbSecretPropertiesNotFoundBySecretLocatorError, T> getCriticalDbSecretProperties(
        CriticalSecretKey<T> key);

    <T> Option<T> getSecret(SecretKey<T> key);

    boolean hasSecret(SecretKey<?> key);

    class ErrorMessage {

        public static final String NO_CRITICAL_DB_SECRET_PROPERTIES_FOUND_FOR_KEY_ERROR_MESSAGE =
            "No critical db secret properties found for key: %s";
    }
}