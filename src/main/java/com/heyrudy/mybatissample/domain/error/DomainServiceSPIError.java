package com.heyrudy.mybatissample.domain.error;

public sealed interface DomainServiceSPIError
    extends DomainError
    permits DomainServiceSPIError.PDFDocumentCreationError {

    record PDFDocumentCreationError(String message)
        implements DomainServiceSPIError {

    }
}