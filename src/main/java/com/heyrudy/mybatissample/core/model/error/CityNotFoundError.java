package com.heyrudy.mybatissample.core.model.error;

public final class CityNotFoundError extends DomainError {
    public CityNotFoundError(String message) {
        super(message);
    }
}
