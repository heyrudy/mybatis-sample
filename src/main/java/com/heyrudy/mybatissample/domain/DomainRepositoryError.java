package com.heyrudy.mybatissample.domain;

public sealed interface DomainRepositoryError
    extends DomainErrorModule.DomainError
    permits CityRepositoryError {

    String message();
}