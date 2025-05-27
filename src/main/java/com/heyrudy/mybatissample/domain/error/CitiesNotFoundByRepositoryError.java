package com.heyrudy.mybatissample.domain.error;

public record CitiesNotFoundByRepositoryError(String message)
    implements CityRepositoryError {

}