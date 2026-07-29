package com.heyrudy.mybatissample.domain;

import com.heyrudy.mybatissample.domain.MissingCriticalSecretError.CriticalDbSecretNotFoundByDependencyLocatorError;
import com.heyrudy.mybatissample.domain.MissingCriticalSecretError.CriticalRestSecretNotFoundByDependencyLocatorError;

public sealed interface MissingCriticalSecretError
    extends DomainErrorModule.MissingCriticalDependencyError
    permits CriticalDbSecretNotFoundByDependencyLocatorError
    , CriticalRestSecretNotFoundByDependencyLocatorError {

    record CriticalDbSecretNotFoundByDependencyLocatorError(String message)
        implements MissingCriticalSecretError {

        public static class ErrorMessage {

            public static final String CRITICAL_DB_SECRET_NOT_FOUND =
                """
                    No critical db secret found: %s
                    """;
        }
    }

    record CriticalRestSecretNotFoundByDependencyLocatorError(String message)
        implements MissingCriticalSecretError {

        public static class ErrorMessage {

            public static final String CRITICAL_REST_SECRET_NOT_FOUND =
                """
                    No critical rest secret found: %s
                    """;
        }
    }
}
