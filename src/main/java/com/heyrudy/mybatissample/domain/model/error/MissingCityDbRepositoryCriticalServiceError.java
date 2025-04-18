package com.heyrudy.mybatissample.domain.model.error;

public final class MissingCityDbRepositoryCriticalServiceError
    extends DomainError
    implements MissingCityError {

    public MissingCityDbRepositoryCriticalServiceError(String message) {
        super(message);
    }
}
