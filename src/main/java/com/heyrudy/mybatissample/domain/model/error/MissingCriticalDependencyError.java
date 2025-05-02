package com.heyrudy.mybatissample.domain.model.error;

public sealed class MissingCriticalDependencyError
    implements DomainError
    permits MissingCriticalSecretError,
    MissingCriticalConfigError,
    CriticalRepositoryNotFoundByServiceLocatorError,
    DbCriticalServiceNotFoundByServiceLocatorError {

    protected String message;

    public MissingCriticalDependencyError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public RuntimeException toException() {
        return new RuntimeException(this.message);
    }
}
