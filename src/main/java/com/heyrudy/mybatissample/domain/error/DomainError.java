package com.heyrudy.mybatissample.domain.error;

public sealed interface DomainError
    permits MissingCriticalDependencyError,
    DomainRepositoryError,
    DomainServiceSPIError,
    DomainServiceAPIError {

}