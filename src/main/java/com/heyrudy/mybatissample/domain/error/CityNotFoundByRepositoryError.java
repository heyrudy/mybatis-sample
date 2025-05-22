package com.heyrudy.mybatissample.domain.error;

public record CityNotFoundByRepositoryError(String message)
    implements CityRepositoryError {

}
