package com.heyrudy.mybatissample.domain.model.error;

public record CitiesNotFoundError(String message)
    implements DomainServiceAPIError {

}
