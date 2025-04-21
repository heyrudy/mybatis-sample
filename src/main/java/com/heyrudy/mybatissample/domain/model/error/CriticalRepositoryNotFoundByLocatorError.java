package com.heyrudy.mybatissample.domain.model.error;

public final class CriticalRepositoryNotFoundByLocatorError
    extends MissingCriticalDependencyError {

    public CriticalRepositoryNotFoundByLocatorError(String message) {
        super(message);
    }
}
