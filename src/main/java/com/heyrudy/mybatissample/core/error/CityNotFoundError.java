package com.heyrudy.mybatissample.core.error;

public final class CityNotFoundError extends DomainError {
    public CityNotFoundError(String message) {
        super(message);
    }
}
