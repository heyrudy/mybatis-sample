package com.heyrudy.mybatissample.domain.model.error;

public sealed class DomainRepositoryError
    implements DomainError
    permits CityRepositoryError {

    protected String message;

    public DomainRepositoryError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
