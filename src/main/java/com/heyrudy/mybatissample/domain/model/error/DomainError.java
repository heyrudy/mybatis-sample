package com.heyrudy.mybatissample.domain.model.error;

public sealed interface DomainError
    permits MissingCriticalDependencyError,
    DomainRepositoryError,
    DomainServiceSPIError,
    DomainServiceAPIError {

}


