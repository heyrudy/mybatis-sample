package com.heyrudy.mybatissample.context;

import com.heyrudy.mybatissample.domain.model.error.CriticalDbSecretPropertiesNotFoundByDependencyLocatorError;
import com.heyrudy.mybatissample.domain.model.error.MissingCriticalDependencyError;
import com.heyrudy.mybatissample.context.IDbSecretProperties.MockedDbSecretProperties;
import cyclops.control.Reader;
import io.vavr.control.Either;
import io.vavr.control.Option;

public enum CriticalDbSecretPropertiesKey
    implements CriticalSecretKey<IDbSecretProperties> {
    INSTANCE;

    @Override
    public Reader<AppScopedDependencyLocator, Either<? extends MissingCriticalDependencyError, IDbSecretProperties>> describeDependencyContext() {
        return appScopedDependencyLocator ->
            Option.of(
                    appScopedDependencyLocator.getDependencyOrMock(
                        IDbSecretProperties.class, Option.of(MockedDbSecretProperties::new)))
                .toEither(new CriticalDbSecretPropertiesNotFoundByDependencyLocatorError(
                    AppScopedDependencyLocator.ErrorMessage.NO_CRITICAL_DB_SECRET_PROPERTIES_FOUND_FOR_KEY_ERROR_MESSAGE
                        .formatted(INSTANCE)));
    }

    @Override
    public String toString() {
        return "CriticalDatabasePropertiesKey{}";
    }
}