package com.heyrudy.mybatissample.domain.model.error;

public sealed interface CityRepositoryError
    extends DomainRepositoryError
    permits CityNotSavedByRepositoryError,
    CityNotFoundByRepositoryError {

}
