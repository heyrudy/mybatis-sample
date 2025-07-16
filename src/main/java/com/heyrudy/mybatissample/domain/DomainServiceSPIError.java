package com.heyrudy.mybatissample.domain;

import com.heyrudy.mybatissample.domain.DomainServiceSPIError.PDFDocumentCreationError;

public sealed interface DomainServiceSPIError
    extends DomainError
    permits PDFDocumentCreationError {

    record PDFDocumentCreationError(String message)
        implements DomainServiceSPIError {

    }
}