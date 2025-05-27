package com.heyrudy.mybatissample.domain.error;

public record CityNotFoundError(String message)
    implements DomainServiceAPIError {

    public static class ErrorMessage {

        public static final String CITY_NOT_FOUND =
            "City with id %d was not found";
    }

    public static class SuccessMessage {

        public static final String CITY_FOUND =
            "A city with id {} is found";
    }
}