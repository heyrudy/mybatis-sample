package com.heyrudy.mybatissample.domain.error;

public record CitiesNotFoundError(String message)
    implements DomainServiceAPIError {

}
