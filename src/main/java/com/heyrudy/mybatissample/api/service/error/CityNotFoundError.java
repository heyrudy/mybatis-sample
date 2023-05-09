package com.heyrudy.mybatissample.api.service.error;

public final class CityNotFoundError extends ApiError {
    public CityNotFoundError(String message) {
        super(message);
    }
}
