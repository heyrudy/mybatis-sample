package com.heyrudy.mybatissample.domain.model.error;

public final class CityNotFoundByRepositoryError
    extends CityRepositoryError {

    public CityNotFoundByRepositoryError(String message) {
        super(message);
    }
}
