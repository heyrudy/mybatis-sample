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
                .reduce(new ApiErrorResponse(), (model, option) -> option.apply(model),
                    (a, b) -> a);
        }

        @SafeVarargs
        public final ApiErrorResponse with(MutatorOption<ApiErrorResponse>... options) {
            return Arrays.stream(options)
                .filter(Objects::nonNull)
                .reduce(this, (model, option) -> option.apply(model), (a, b) -> a);
        }

        public enum ApiErrorResponseMutatorOptions {
            INSTANCE;

            public MutatorOption<ApiErrorResponse> reason(String reason) {
                return MutatorOption.of(
                    reason,
                    (it, v) -> new ApiErrorResponse(v)
                );
            }
        }
    }
}
