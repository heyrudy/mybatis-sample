package com.heyrudy.mybatissample.api.service.error;

public sealed class ApiError permits CityNotFoundError {
    protected String message;

    public ApiError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public RuntimeException toException() {
        return new RuntimeException(this.message);
    }
}


