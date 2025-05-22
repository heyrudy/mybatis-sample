package com.heyrudy.mybatissample.domain.model.error;

public record CriticalDbSecretPropertiesNotFoundByDependencyLocatorError(String message)
    implements MissingCriticalSecretError {

}
