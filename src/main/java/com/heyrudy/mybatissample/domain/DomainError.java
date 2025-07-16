package com.heyrudy.mybatissample.domain;

public sealed interface DomainError
    permits MissingCriticalDependencyError,
    DomainRepositoryError,
    DomainServiceSPIError,
    DomainServiceAPIError {

}