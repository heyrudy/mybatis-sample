package com.heyrudy.mybatissample.domain.model.error;

public final class CriticalRepositoryNotFoundByServiceLocatorError
    extends MissingCriticalDependencyError {

    public CriticalRepositoryNotFoundByServiceLocatorError(String message) {
        super(message);
    }
}
