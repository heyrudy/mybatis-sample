package com.heyrudy.mybatissample.domain.model.error;

public final class CriticalDbSecretPropertiesNotFoundBySecretLocatorError
    extends MissingCriticalSecretError {

    public CriticalDbSecretPropertiesNotFoundBySecretLocatorError(String message) {
        super(message);
    }
}
