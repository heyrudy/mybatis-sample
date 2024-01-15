package com.heyrudy.mybatissample.domain.model;

import java.io.IOException;

public class ApplicationRuntimeException extends RuntimeException {

    public ApplicationRuntimeException(String errorMessage, Exception innerException) {
        super(errorMessage, innerException);
    }

    public ApplicationRuntimeException(Exception innerException) {
        super(innerException);
    }
}
