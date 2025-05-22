package com.heyrudy.mybatissample.domain.model.error;

public record CriticalRepositoryNotFoundByDependencyLocatorError(String message)
    implements MissingCriticalDependencyError {

    public RuntimeException toException() {
        return new RuntimeException(this.message);
    }
}
