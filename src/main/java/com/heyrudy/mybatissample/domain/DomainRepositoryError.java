package com.heyrudy.mybatissample.domain;

public sealed interface DomainRepositoryError
    extends DomainError
    permits CityRepositoryError {

    String message();
}