package com.heyrudy.mybatissample.domain.model;

public class ApplicationRuntimeException extends RuntimeException {

    public ApplicationRuntimeException(String errorMessage, Exception innerException) {
        super(errorMessage, innerException);
    }
}
