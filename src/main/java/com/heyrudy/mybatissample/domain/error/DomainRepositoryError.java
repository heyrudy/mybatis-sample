package com.heyrudy.mybatissample.domain.error;

public sealed interface DomainRepositoryError
    extends DomainError
    permits CityRepositoryError {

    String message();
}
