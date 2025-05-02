package com.heyrudy.mybatissample.domain.model.error;

public sealed class MissingCriticalConfigError
    extends MissingCriticalDependencyError
    permits CriticalDataSourceNotFoundByConfigLocatorError,
    CriticalDSLContextNotFoundByConfigLocatorError {

    public MissingCriticalConfigError(String message) {
        super(message);
        this.message = message;
    }
}
