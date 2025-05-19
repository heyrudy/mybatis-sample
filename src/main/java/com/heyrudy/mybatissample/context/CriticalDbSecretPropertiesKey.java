package com.heyrudy.mybatissample.context;

import com.heyrudy.mybatissample.context.IDbSecretProperties.MockedDbSecretProperties;
import com.heyrudy.mybatissample.domain.model.error.CriticalDbSecretPropertiesNotFoundByDependencyLocatorError;
import com.heyrudy.mybatissample.domain.model.error.MissingCriticalDependencyError;
import cyclops.control.Reader;
import io.vavr.control.Either;
import io.vavr.control.Option;

public enum CriticalDbSecretPropertiesKey
    implements CriticalSecretKey<IDbSecretProperties> {
    INSTANCE;

    private static final CriticalDbSecretPropertiesNotFoundByDependencyLocatorError CRITICAL_DB_SECRET_PROPERTIES_NOT_FOUND_BY_DEPENDENCY_LOCATOR_PATH =
        new CriticalDbSecretPropertiesNotFoundByDependencyLocatorError(
            AppScopedDependencyLocator.ErrorMessage.NO_CRITICAL_DB_SECRET_PROPERTIES_FOUND_FOR_KEY_ERROR_MESSAGE
                .formatted(INSTANCE));

    @Override
    public Reader<AppScopedDependencyLocator, Either<? extends MissingCriticalDependencyError, IDbSecretProperties>> describeDependencyContext() {
        return appScopedDependencyLocator ->
            Option.of(
                    appScopedDependencyLocator.getDependencyOrMock(
                        IDbSecretProperties.class, Option.of(MockedDbSecretProperties::new)))
                .toEither(CRITICAL_DB_SECRET_PROPERTIES_NOT_FOUND_BY_DEPENDENCY_LOCATOR_PATH);
    }

    @Override
    public String toString() {
        return "CriticalDatabasePropertiesKey{}";
    }
}