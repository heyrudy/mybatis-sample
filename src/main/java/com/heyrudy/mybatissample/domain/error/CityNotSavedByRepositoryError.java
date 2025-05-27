package com.heyrudy.mybatissample.domain.error;

public record CityNotSavedByRepositoryError(String message)
    implements CityRepositoryError {

    public static class ErrorMessage {

        public static final String CITY_NOT_SAVED =
            "Failed to insert city: No record returned";
    }
}