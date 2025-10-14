package com.heyrudy.mybatissample.application;

import com.heyrudy.mybatissample.domain.UtilsModule;
import java.util.Arrays;
import java.util.Objects;

public interface APIErrorModule extends UtilsModule {

    record ApiErrorResponse(String reason) {

        public ApiErrorResponse() {
            this("");
        }

        @SafeVarargs
        public static ApiErrorResponse of(MutatorOption<ApiErrorResponse>... options) {
            return Arrays.stream(options)
                .filter(Objects::nonNull)
                .reduce(new ApiErrorResponse(), (dto, option) -> option.apply(dto),
                    (a, b) -> a);
        }
    }

    enum ApiErrorResponseMutatorOptions {
        INSTANCE;

        public MutatorOption<ApiErrorResponse> reason(String reason) {
            return MutatorOption.of(
                reason,
                (it, v) -> new ApiErrorResponse(v)
            );
        }
    }
}
