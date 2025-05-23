package com.heyrudy.mybatissample.context;

import com.heyrudy.mybatissample.domain.error.MissingCriticalDependencyError;
import com.heyrudy.mybatissample.domain.spi.IAuditSPI;
import cyclops.control.Reader;
import io.vavr.control.Either;

public enum AuditSPIKey
    implements NonCriticalSPIKey<IAuditSPI> {
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