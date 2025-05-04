package com.heyrudy.mybatissample.domain.model.error;

public final class CriticalDbSecretPropertiesNotFoundByDependencyLocatorError
    extends MissingCriticalSecretError {

    public CriticalDbSecretPropertiesNotFoundByDependencyLocatorError(String message) {
        super(message);
    }
}
