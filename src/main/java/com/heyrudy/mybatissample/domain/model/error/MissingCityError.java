package com.heyrudy.mybatissample.domain.model.error;

public sealed class MissingCityError
    extends DomainServiceAPIError
    permits CityNotFoundError, MissingCityDbCriticalServiceError {

    public MissingCityError(String message) {
        super(message);
    }
}
