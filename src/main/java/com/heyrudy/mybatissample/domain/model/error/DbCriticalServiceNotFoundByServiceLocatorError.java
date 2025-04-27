package com.heyrudy.mybatissample.domain.model.error;

public final class DbCriticalServiceNotFoundByServiceLocatorError
        extends MissingCriticalDependencyError {

    public DbCriticalServiceNotFoundByServiceLocatorError(String message) {
        super(message);
    }
}