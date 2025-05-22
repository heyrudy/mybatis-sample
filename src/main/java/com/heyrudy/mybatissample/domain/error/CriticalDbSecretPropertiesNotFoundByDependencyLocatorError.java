package com.heyrudy.mybatissample.domain.error;

public record CriticalDbSecretPropertiesNotFoundByDependencyLocatorError(String message)
    implements MissingCriticalSecretError {

}
