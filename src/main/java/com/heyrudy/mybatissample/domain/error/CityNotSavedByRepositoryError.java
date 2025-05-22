package com.heyrudy.mybatissample.domain.error;

public record CityNotSavedByRepositoryError(String message)
    implements CityRepositoryError {

}
