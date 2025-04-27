package com.heyrudy.mybatissample.domain.model.error;

public sealed class MissingCriticalConfigError
    implements DomainError
    permits CriticalDataSourceNotFoundByConfigLocatorError,
    CriticalDSLContextNotFoundByConfigLocatorError {

    protected String message;

    public MissingCriticalConfigError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public RuntimeException toException() {
        return new RuntimeException(this.message);
    }
}
