package com.heyrudy.mybatissample.domain.model.error;

public record CriticalDSLContextNotFoundByDependencyLocatorError(String message)
    implements MissingCriticalConfigError {

}
