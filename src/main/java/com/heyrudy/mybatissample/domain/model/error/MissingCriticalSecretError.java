package com.heyrudy.mybatissample.domain.model.error;

public sealed class MissingCriticalSecretError
    extends MissingCriticalDependencyError
    permits CriticalDbSecretPropertiesNotFoundBySecretLocatorError {

    public MissingCriticalSecretError(String message) {
        super(message);
        this.message = message;
    }
}
