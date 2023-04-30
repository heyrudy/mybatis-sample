package com.heyrudy.mybatissample.core.error;

public sealed class DomainError
        permits CityNotFoundError {
    protected String message;

    public DomainError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public RuntimeException toException() {
        return new RuntimeException(this.message);
    }
}


