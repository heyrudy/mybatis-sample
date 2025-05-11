package com.heyrudy.mybatissample.domain.model.error;

public final class CityNotFoundError
    extends DomainServiceAPIError {

    public CityNotFoundError(String message) {
        super(message);
    }

    public static class ErrorMessage {

        public static final String CITY_NOT_FOUND_ERROR_MESSAGE =
            "City with id %d was not found";
    }

    public static class SuccessMessage {

        public static final String CITY_FOUND_SUCCESS_MESSAGE =
            "A city with id {} is found";
    }
}
