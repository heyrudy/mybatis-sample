package com.heyrudy.mybatissample.domain.model.error;

public final class MissingCityDbCriticalServiceError
    extends DomainError
    implements MissingCityError {

    public MissingCityDbCriticalServiceError(String message) {
        super(message);
    }
}
