package com.heyrudy.mybatissample.context;

import com.heyrudy.mybatissample.domain.model.error.MissingCriticalDependencyError;
import com.heyrudy.mybatissample.domain.spi.IAuditSPI;
import cyclops.control.Reader;
import io.vavr.control.Either;

public enum AuditSPIKey
    implements NonCriticalSPIKey<IAuditSPI> {
    INSTANCE;

    @Override
    public Reader<AppScopedDependencyLocator, Either<MissingCriticalDependencyError, IAuditSPI>> describeDependencyContext() {
        return null;
    }

    @Override
    public String toString() {
        return "AuditSPIKey{}";
    }
}