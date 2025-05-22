package com.heyrudy.mybatissample.domain.error;

public sealed interface MissingCriticalSecretError
    extends MissingCriticalDependencyError
    permits CriticalDbSecretPropertiesNotFoundByDependencyLocatorError {

}
