package com.heyrudy.mybatissample.application.context;

import com.heyrudy.mybatissample.domain.MissingCriticalDependencyError;
import cyclops.control.Reader;
import io.vavr.control.Either;

public sealed interface DependencyKey<T>
    extends EnvironmentKey
    permits ConfigKey,
    CriticalRepositoryKey,
    NonCriticalSPIKey {

    Reader<AppScopedDependencyLocator, Either<MissingCriticalDependencyError, T>> lazyLoad();
}