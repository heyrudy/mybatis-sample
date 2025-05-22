package com.heyrudy.mybatissample.domain.model.error;

public sealed interface DomainServiceSPIError
    extends DomainError
    permits PDFDocumentCreationError {

}
