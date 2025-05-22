package com.heyrudy.mybatissample.domain.model.error;

public record PDFDocumentCreationError(String message)
    implements DomainServiceSPIError {

}
