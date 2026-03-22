package com.heyrudy.mybatissample.application.context;

import com.heyrudy.mybatissample.domain.DomainErrorModule.MissingCriticalDependencyError;
import com.heyrudy.mybatissample.gateway.AuditModule;
import com.heyrudy.mybatissample.gateway.AuditModule.IAuditSPI;
import cyclops.control.Reader;
import io.vavr.control.Either;

public enum AuditSPIKey
    implements NonCriticalSPIKey<AuditModule.IAuditSPI> {
    INSTANCE;

    @Override
    public Reader<AppScopedDependencyLocator, Either<MissingCriticalDependencyError, IAuditSPI>> lazyLoad() {
        return null;
    }

    @Override
    public String toString() {
        return "AuditSPIKey{}";
    }
}