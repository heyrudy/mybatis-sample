package com.heyrudy.mybatissample.context;

import com.heyrudy.mybatissample.context.IDbSecret.MockedDbSecret;
import com.heyrudy.mybatissample.domain.error.CriticalDbSecretNotFoundByDependencyLocatorError;
import com.heyrudy.mybatissample.domain.error.CriticalDbSecretNotFoundByDependencyLocatorError.ErrorMessage;
import com.heyrudy.mybatissample.domain.error.MissingCriticalDependencyError;
import cyclops.control.Reader;
import io.vavr.control.Either;
import io.vavr.control.Option;

public enum CriticalDbSecretKey
    implements CriticalSecretKey<IDbSecret> {
    INSTANCE;

    private static final CriticalDbSecretNotFoundByDependencyLocatorError CRITICAL_DB_SECRET_PROPERTIES_NOT_FOUND_BY_DEPENDENCY_LOCATOR_PATH =
        new CriticalDbSecretNotFoundByDependencyLocatorError(
            ErrorMessage.CRITICAL_DB_SECRET_NOT_FOUND.formatted(INSTANCE));

    @Override
    public Reader<AppScopedDependencyLocator, Either<MissingCriticalDependencyError, IDbSecret>> lazyLoad() {
        return appScopedDependencyLocator ->
            Option.of(
                    appScopedDependencyLocator.getDependency(
                        IDbSecret.class, Option.of(MockedDbSecret::new)))
                .toEither(CRITICAL_DB_SECRET_PROPERTIES_NOT_FOUND_BY_DEPENDENCY_LOCATOR_PATH);
    }

    @Override
    public String toString() {
        return "CriticalDbSecretKey{}";
    }
}