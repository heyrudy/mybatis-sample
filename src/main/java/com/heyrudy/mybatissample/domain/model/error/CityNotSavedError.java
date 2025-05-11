package com.heyrudy.mybatissample.domain.model.error;

public final class CityNotSavedError
    extends DomainServiceAPIError {

    public CityNotSavedError(String message) {
        super(message);
    }

    public static class ErrorMessage {

        public static final String CITY_NOT_SAVED_ERROR_MESSAGE =
            """
                City with details:
                {}
                was not saved""";
    }

    public static class SuccessMessage {

        public static final String CITY_SAVED_SUCCESS_MESSAGE =
            "A city  is saved with id {}";
    }
}
