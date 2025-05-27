package com.heyrudy.mybatissample.domain.error;

public sealed interface MissingCriticalConfigError
    extends MissingCriticalDependencyError
    permits CriticalDSLContextNotFoundByDependencyLocatorError {

}