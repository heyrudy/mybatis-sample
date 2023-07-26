package com.heyrudy.mybatissample.controller.rest.dto;

public record ApiErrorResponse(String reason) {

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
