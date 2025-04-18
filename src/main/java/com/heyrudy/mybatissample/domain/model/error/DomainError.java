package com.heyrudy.mybatissample.domain.model.error;

public sealed class DomainError
    permits CityNotFoundError,
    MissingCityDbRepositoryCriticalServiceError,
    PDFDocumentCreationError,
    DbCriticalServiceNotFoundByLocatorError,
    CriticalRepositoryNotFoundByLocatorError {

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


