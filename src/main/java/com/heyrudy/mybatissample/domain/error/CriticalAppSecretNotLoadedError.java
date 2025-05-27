package com.heyrudy.mybatissample.domain.error;

public record CriticalAppSecretNotLoadedError(String message)
    implements MissingCriticalSecretError {

    public static class ErrorMessage {

        public static final String CRITICAL_APP_SECRET_NOT_LOADED_ERROR_MESSAGE =
            """
                No critical app secret loaded: %s
                with reason: %s
                """;
    }
}