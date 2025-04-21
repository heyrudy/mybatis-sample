package com.heyrudy.mybatissample.domain.model.error;

public sealed class DomainServiceSPIError
    implements DomainError
    permits PDFDocumentCreationError {

    protected String message;

    public DomainServiceSPIError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public RuntimeException toException() {
        return new RuntimeException(this.message);
    }
}
