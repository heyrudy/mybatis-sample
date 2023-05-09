package com.heyrudy.mybatissample.api.controller.utils;

import lombok.Getter;
import lombok.Value;

import java.util.Objects;

@Getter
@Value
public class Result<T> {
    T value;
    String error;

    private Result(T value, String error) {
        this.value = value;
        this.error = error;
    }

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
