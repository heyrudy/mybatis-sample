package com.heyrudy.mybatissample.application.rest;

public interface APIErrorModule {

    record ApiErrorResponse(String reason) {

        public static ApiErrorResponseBuilder builder() {
            return new ApiErrorResponseBuilder();
        }

        public static class ApiErrorResponseBuilder {

            private String reason;

            public ApiErrorResponseBuilder() {
            }

            public ApiErrorResponseBuilder reason(String reason) {
                this.reason = reason;
                return this;
            }

            public ApiErrorResponse build() {
                return new ApiErrorResponse(reason);
            }
        }
    }
}
