package com.heyrudy.mybatissample.controller.utils;

import java.util.Objects;

public record Result<T>(T value, String error) {

    public static <T> Result<T> of(T value, String error) {
        return new Result<>(Objects.requireNonNull(value), error);
    }

    public static <U> Result<U> ok(U value) {
        return new Result<>(value, null);
    }

    public static <U> Result<U> error(String error) {
        return new Result<>(null, error);
    }

    public boolean isError() {
        return Objects.nonNull(error);
    }
}
