package com.heyrudy.mybatissample.context;

import com.heyrudy.mybatissample.domain.error.MissingCriticalDependencyError;
import cyclops.control.Reader;
import io.vavr.control.Either;

public sealed interface DependencyKey<T>
    extends EnvironmentKey
    permits ConfigKey,
    CriticalRepositoryKey,
    NonCriticalSPIKey {

    Reader<AppScopedDependencyLocator, Either<MissingCriticalDependencyError, T>> lazyLoad();
}
