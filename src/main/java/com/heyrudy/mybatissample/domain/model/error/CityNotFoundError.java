package com.heyrudy.mybatissample.domain.model.error;

public final class CityNotFoundError
    extends DomainError
    implements MissingCityError {

    public CityNotFoundError(String message) {
        super(message);
    }

    public static class ErrorMessage {

        public static final String CITY_NOT_FOUND_ERROR_MESSAGE =
            "City with id %d was not found";
    }
}
