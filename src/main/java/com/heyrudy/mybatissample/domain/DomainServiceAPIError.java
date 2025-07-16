package com.heyrudy.mybatissample.domain;

import com.heyrudy.mybatissample.domain.DomainServiceAPIError.CitiesNotFoundError;
import com.heyrudy.mybatissample.domain.DomainServiceAPIError.CityNotFoundError;
import com.heyrudy.mybatissample.domain.DomainServiceAPIError.CityNotSavedError;

public sealed interface DomainServiceAPIError
    extends DomainError
    permits CitiesNotFoundError,
    CityNotFoundError,
    CityNotSavedError {

    String message();

    record CityNotFoundError(String message)
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

    record CitiesNotFoundError(String message)
        implements DomainServiceAPIError {

    }

    record CityNotSavedError(String message)
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
}