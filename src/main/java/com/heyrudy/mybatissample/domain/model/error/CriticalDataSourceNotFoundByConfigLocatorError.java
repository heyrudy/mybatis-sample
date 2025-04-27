package com.heyrudy.mybatissample.domain.model.error;

public final class CriticalDataSourceNotFoundByConfigLocatorError
    extends MissingCriticalConfigError {

    public CriticalDataSourceNotFoundByConfigLocatorError(String message) {
        super(message);
    }
}
