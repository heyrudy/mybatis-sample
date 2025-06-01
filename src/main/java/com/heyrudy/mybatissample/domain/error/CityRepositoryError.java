package com.heyrudy.mybatissample.domain.error;

public sealed interface CityRepositoryError
    extends DomainRepositoryError
    permits CityRepositoryError.CityTableNotTruncatedError,
    CityRepositoryError.CitiesNotFoundByRepositoryError,
    CityRepositoryError.CityNotFoundByRepositoryError,
    CityRepositoryError.CityNotSavedByRepositoryError {

    record CityNotFoundByRepositoryError(String message)
        implements CityRepositoryError {

        public static class ErrorMessage {

            public static final String CITY_NOT_FOUND_BY_ID =
                "Failed to retrieve city with ID %d";
        }
    }

    record CitiesNotFoundByRepositoryError(String message)
        implements CityRepositoryError {

    }

    record CityNotSavedByRepositoryError(String message)
        implements CityRepositoryError {

        public static class ErrorMessage {

            public static final String CITY_NOT_SAVED =
                "Failed to insert city: No record returned";
        }
    }

    record CityTableNotTruncatedError(String message)
        implements CityRepositoryError {

        public static class ErrorMessage {

            public static final String CITY_TABLE_NOT_TRUNCATED =
                """
                    Failed to truncate city table.
                    reason: %s""";
        }
    }
}