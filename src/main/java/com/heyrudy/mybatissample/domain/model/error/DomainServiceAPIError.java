package com.heyrudy.mybatissample.domain.model.error;

public sealed interface DomainServiceAPIError
    extends DomainError
    permits CitiesNotFoundError,
    CityNotFoundError,
    CityNotSavedError {

    String message();
}
