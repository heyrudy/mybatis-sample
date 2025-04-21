package com.heyrudy.mybatissample.domain.model.error;

public sealed class DomainServiceAPIError
    implements DomainError
    permits MissingCityError {

    protected String message;

    public DomainServiceAPIError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
