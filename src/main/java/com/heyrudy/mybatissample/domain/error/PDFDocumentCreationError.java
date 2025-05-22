package com.heyrudy.mybatissample.domain.error;

public record PDFDocumentCreationError(String message)
    implements DomainServiceSPIError {

}
