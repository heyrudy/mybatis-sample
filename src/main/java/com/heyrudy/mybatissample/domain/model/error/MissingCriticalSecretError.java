package com.heyrudy.mybatissample.domain.model.error;

public sealed class MissingCriticalSecretError
    implements DomainError
    permits CriticalDbSecretPropertiesNotFoundBySecretLocatorError {

    protected String message;

    public MissingCriticalSecretError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public RuntimeException toException() {
        return new RuntimeException(this.message);
    }
}
