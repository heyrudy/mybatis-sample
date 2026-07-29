package com.heyrudy.mybatissample.application.context;

import com.heyrudy.mybatissample.domain.DomainErrorModule.MissingCriticalDependencyError;
import com.heyrudy.mybatissample.gateway.AuditModule.AuditAdapterResolver;
import com.heyrudy.mybatissample.gateway.AuditModule.IAuditSPI;
import cyclops.control.Reader;
import io.vavr.control.Either;

public enum AuditSPIKey
    implements NonCriticalSPIKey<IAuditSPI> {
    INSTANCE;

    @Override
    public Reader<AppScopedDependencyLocator, Either<MissingCriticalDependencyError, IAuditSPI>> lazyLoad() {
        return _ -> Either.right(AuditAdapterResolver.INSTANCE.resolve());
    }

    @Override
    public String toString() {
        return "AuditSPIKey{}";
    }
}
