package com.heyrudy.mybatissample.domain.model.error;

public final class CityNotSavedByRepositoryError
    extends CityRepositoryError {

    public CityNotSavedByRepositoryError(String message) {
        super(message);
    }
}
