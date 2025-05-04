package com.heyrudy.mybatissample.domain.model.error;

public sealed class MissingCriticalConfigError
    extends MissingCriticalDependencyError
    permits CriticalDataSourceNotFoundByDependencyLocatorError,
        CriticalDSLContextNotFoundByDependencyLocatorError {

    public MissingCriticalConfigError(String message) {
        super(message);
        this.message = message;
    }
}
