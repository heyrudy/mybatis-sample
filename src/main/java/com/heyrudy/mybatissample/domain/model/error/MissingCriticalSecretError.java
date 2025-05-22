package com.heyrudy.mybatissample.domain.model.error;

public sealed interface MissingCriticalSecretError
    extends MissingCriticalDependencyError
    permits CriticalDbSecretPropertiesNotFoundByDependencyLocatorError {

}
