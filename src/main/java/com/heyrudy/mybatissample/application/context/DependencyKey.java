package com.heyrudy.mybatissample.application.context;

import com.heyrudy.mybatissample.domain.DomainErrorModule;
import cyclops.control.Reader;
import io.vavr.control.Either;

public sealed interface DependencyKey<T>
    extends CapabilityKey<T>
    permits ConfigKey
    , CriticalRepositoryKey
    , NonCriticalSPIKey {

    Reader<AppScopedDependencyLocator, Either<DomainErrorModule.MissingCriticalDependencyError, T>> lazyLoad();
}