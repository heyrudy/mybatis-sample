package com.heyrudy.mybatissample.domain.model.error;

public record CityNotSavedByRepositoryError(String message)
    implements CityRepositoryError {

}
