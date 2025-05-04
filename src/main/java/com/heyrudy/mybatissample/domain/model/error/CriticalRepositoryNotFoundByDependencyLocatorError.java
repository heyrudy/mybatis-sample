package com.heyrudy.mybatissample.domain.model.error;

public final class CriticalRepositoryNotFoundByDependencyLocatorError
    extends MissingCriticalDependencyError {

    public CriticalRepositoryNotFoundByDependencyLocatorError(String message) {
        super(message);
        this.message = message;
    }
}
