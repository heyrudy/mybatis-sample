package com.heyrudy.mybatissample.domain.model.error;

public sealed interface MissingCriticalConfigError
    extends MissingCriticalDependencyError
    permits CriticalDSLContextNotFoundByDependencyLocatorError {

}
