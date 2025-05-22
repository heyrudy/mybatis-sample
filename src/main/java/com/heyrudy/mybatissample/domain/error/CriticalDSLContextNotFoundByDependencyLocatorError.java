package com.heyrudy.mybatissample.domain.error;

public record CriticalDSLContextNotFoundByDependencyLocatorError(String message)
    implements MissingCriticalConfigError {

}
