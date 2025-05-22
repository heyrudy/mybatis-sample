package com.heyrudy.mybatissample.domain.model.error;

public sealed interface MissingCriticalDependencyError
    extends DomainError
    permits MissingCriticalSecretError,
    MissingCriticalConfigError,
    CriticalRepositoryNotFoundByDependencyLocatorError {

    String message();
}
