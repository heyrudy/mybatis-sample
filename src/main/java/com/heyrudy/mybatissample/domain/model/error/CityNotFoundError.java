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
}
