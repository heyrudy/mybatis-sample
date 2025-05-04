package com.heyrudy.mybatissample.domain.model.error;

public final class CriticalDataSourceNotFoundByDependencyLocatorError
    extends MissingCriticalConfigError {

    public CriticalDataSourceNotFoundByDependencyLocatorError(String message) {
        super(message);
    }
}
