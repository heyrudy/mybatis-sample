package com.heyrudy.mybatissample.domain.error;

public record CityNotFoundByRepositoryError(String message)
    implements CityRepositoryError {

    public static class ErrorMessage {

        public static final String CITY_NOT_FOUND_BY_ID =
            "Failed to retrieve city with ID %d";
    }
}