package com.heyrudy.mybatissample.domain.error;

public sealed interface DomainServiceSPIError
    extends DomainError
    permits PDFDocumentCreationError {

}