package com.heyrudy.mybatissample.domain.error;

public record CriticalRepositoryNotFoundByDependencyLocatorError(String message)
    implements MissingCriticalDependencyError {

    public RuntimeException toException() {
        return new RuntimeException(this.message);
    }
}
