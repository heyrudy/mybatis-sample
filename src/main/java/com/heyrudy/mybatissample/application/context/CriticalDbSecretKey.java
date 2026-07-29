package com.heyrudy.mybatissample.application.context;

import com.heyrudy.mybatissample.application.context.IDbSecret.MockedDbSecret;
import com.heyrudy.mybatissample.domain.DomainErrorModule;
import com.heyrudy.mybatissample.domain.MissingCriticalSecretError.CriticalDbSecretNotFoundByDependencyLocatorError;
import com.heyrudy.mybatissample.domain.MissingCriticalSecretError.CriticalDbSecretNotFoundByDependencyLocatorError.ErrorMessage;
import cyclops.control.Reader;
import io.vavr.control.Either;
import io.vavr.control.Option;

public enum CriticalDbSecretKey
    implements CriticalSecretKey<IDbSecret> {
    INSTANCE;

    private static final CriticalDbSecretNotFoundByDependencyLocatorError CRITICAL_DB_SECRET_NOT_FOUND_BY_DEPENDENCY_LOCATOR =
        new CriticalDbSecretNotFoundByDependencyLocatorError(
            ErrorMessage.CRITICAL_DB_SECRET_NOT_FOUND.formatted(INSTANCE));

    @Override
    public Reader<AppScopedDependencyLocator, Either<DomainErrorModule.MissingCriticalDependencyError, IDbSecret>> lazyLoad() {
        return appScopedDependencyLocator ->
            Option.of(
                    appScopedDependencyLocator.getDependency(
                        IDbSecret.class, Option.of(MockedDbSecret::new)))
                .toEither(CRITICAL_DB_SECRET_NOT_FOUND_BY_DEPENDENCY_LOCATOR);
    }

    @Override
    public String toString() {
        return "CriticalDbSecretKey{}";
    }
}
