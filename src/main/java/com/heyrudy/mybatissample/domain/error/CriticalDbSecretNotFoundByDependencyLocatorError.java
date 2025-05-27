package com.heyrudy.mybatissample.domain.error;

public record CriticalDbSecretNotFoundByDependencyLocatorError(String message)
    implements MissingCriticalSecretError {

    public static class ErrorMessage {

        public static final String CRITICAL_DB_SECRET_NOT_FOUND =
            """
                No critical db secret found: %s
                """;
    }
}