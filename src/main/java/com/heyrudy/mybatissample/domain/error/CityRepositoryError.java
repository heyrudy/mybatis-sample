package com.heyrudy.mybatissample.domain.error;

public sealed interface CityRepositoryError
    extends DomainRepositoryError
    permits CityTableNotTruncatedError,
    CitiesNotFoundByRepositoryError,
    CityNotFoundByRepositoryError,
    CityNotSavedByRepositoryError {

}