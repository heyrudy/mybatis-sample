package com.heyrudy.mybatissample.domain.model.error;

public record CityNotFoundByRepositoryError(String message)
    implements CityRepositoryError {

}
