package com.heyrudy.mybatissample.domain.model.error;

public final class DbCriticalServiceNotFoundByLocatorError
        extends MissingCriticalDependencyError {

    public DbCriticalServiceNotFoundByLocatorError(String message) {
        super(message);
    }
}