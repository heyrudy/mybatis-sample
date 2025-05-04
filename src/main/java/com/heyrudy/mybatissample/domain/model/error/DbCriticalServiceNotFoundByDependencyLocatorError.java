package com.heyrudy.mybatissample.domain.model.error;

public final class DbCriticalServiceNotFoundByDependencyLocatorError
    extends MissingCriticalDependencyError {

    public DbCriticalServiceNotFoundByDependencyLocatorError(String message) {
        super(message);
        this.message = message;
    }
}