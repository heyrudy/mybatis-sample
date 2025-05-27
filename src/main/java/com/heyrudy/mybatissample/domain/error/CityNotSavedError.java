package com.heyrudy.mybatissample.domain.error;

public record CityNotSavedError(String message)
    implements DomainServiceAPIError {

    public static class ErrorMessage {

        public static final String CITY_NOT_SAVED =
            """
                City with details:
                {}
                was not saved""";
    }

    public static class SuccessMessage {

        public static final String CITY_SAVED =
            "A city  is saved with id {}";
    }
}