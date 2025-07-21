package com.heyrudy.mybatissample.domain;

public sealed interface MissingCriticalConfigError
    extends DomainErrorModule.MissingCriticalDependencyError
    permits MissingCriticalConfigError.CriticalDSLContextNotFoundByDependencyLocatorError {

    record CriticalDSLContextNotFoundByDependencyLocatorError(String message)
        implements MissingCriticalConfigError {

        public static class ErrorMessage {

            public static final String NO_CRITICAL_DSL_CONTEXT_CONFIG_FOUND_FOR_KEY =
                "No critical dsl context config found for key: %s";
        }
    }
}