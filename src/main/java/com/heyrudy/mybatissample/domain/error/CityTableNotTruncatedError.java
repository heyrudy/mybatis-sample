package com.heyrudy.mybatissample.domain.error;

public record CityTableNotTruncatedError(String message)
    implements CityRepositoryError {

    public static class ErrorMessage {

        public static final String CITY_TABLE_NOT_TRUNCATED =
            """
                Failed to truncate city table.
                reason: %s""";
    }
}