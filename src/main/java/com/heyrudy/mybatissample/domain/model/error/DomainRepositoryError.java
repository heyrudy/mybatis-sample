package com.heyrudy.mybatissample.domain.model.error;

public sealed interface DomainRepositoryError
    extends DomainError
    permits CityRepositoryError {

    String message();
}
