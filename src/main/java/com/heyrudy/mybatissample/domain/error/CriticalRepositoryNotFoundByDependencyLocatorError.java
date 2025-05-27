package com.heyrudy.mybatissample.domain.error;

public record CriticalRepositoryNotFoundByDependencyLocatorError(String message)
    implements MissingCriticalDependencyError {

    public RuntimeException toException() {
        return new RuntimeException(this.message);
    }

    public static class ErrorMessage {

        public static final String NO_CRITICAL_REPOSITORY_FOUND_FOR_KEY =
            "No critical repository found for key: %s";
    }
}