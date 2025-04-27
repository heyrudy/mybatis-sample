package com.heyrudy.mybatissample.domain.model.error;

public sealed class CityRepositoryError
    extends DomainRepositoryError
    permits CityNotSavedByRepositoryError,
    CityNotFoundByRepositoryError {

    public CityRepositoryError(String message) {
        super(message);
    }
}
