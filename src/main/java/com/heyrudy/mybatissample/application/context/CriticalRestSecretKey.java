package com.heyrudy.mybatissample.application.context;

import com.heyrudy.mybatissample.domain.DomainErrorModule.MissingCriticalDependencyError;
import cyclops.control.Reader;
import io.vavr.control.Either;

public enum CriticalRestSecretKey
    implements CriticalSecretKey<IRestSecret> {
    INSTANCE;

    @Override
    public Reader<AppScopedDependencyLocator, Either<MissingCriticalDependencyError, IRestSecret>> lazyLoad() {
        return null;
    }

    @Override
    public String toString() {
        return "CriticalRestSecretKey{}";
    }
}
