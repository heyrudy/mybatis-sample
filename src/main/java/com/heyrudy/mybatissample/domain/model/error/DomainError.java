package com.heyrudy.mybatissample.domain.model.error;

public sealed interface DomainError
    permits MissingCriticalSecretError,
    MissingCriticalConfigError,
    MissingCriticalDependencyError,
    DomainRepositoryError,
    DomainServiceSPIError,
    DomainServiceAPIError {

}


