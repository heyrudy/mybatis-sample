package com.heyrudy.mybatissample.domain;

import com.heyrudy.mybatissample.domain.MissingCriticalSecretError.CriticalDbSecretNotFoundByDependencyLocatorError;

public sealed interface MissingCriticalSecretError
    extends MissingCriticalDependencyError
    permits CriticalDbSecretNotFoundByDependencyLocatorError {

    record CriticalDbSecretNotFoundByDependencyLocatorError(String message)
        implements MissingCriticalSecretError {

        public static class ErrorMessage {

            public static final String CRITICAL_DB_SECRET_NOT_FOUND =
                """
                    No critical db secret found: %s
                    """;
        }
    }
}