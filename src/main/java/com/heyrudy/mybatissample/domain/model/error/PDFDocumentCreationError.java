package com.heyrudy.mybatissample.domain.model.error;

public final class PDFDocumentCreationError
    extends DomainServiceSPIError {

    public PDFDocumentCreationError(String message) {
        super(message);
    }
}
