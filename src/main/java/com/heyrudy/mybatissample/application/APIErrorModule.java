package com.heyrudy.mybatissample.application;

import com.heyrudy.mybatissample.domain.UtilsModule;
import java.util.Objects;
import java.util.stream.Stream;

public interface APIErrorModule extends UtilsModule {

    record ApiErrorResponse(String reason) {

        public static ApiErrorResponse empty() {
            return new ApiErrorResponse("");
        }

        @SafeVarargs
        public static ApiErrorResponse of(final MutatorStage<ApiErrorResponse>... stages) {
            return Stream.of(stages)
                .filter(Objects::nonNull)
                .reduce(
                    ApiErrorResponse.empty(),
                    (acc, stage) -> stage.apply(acc),
                    (_, right) -> right);
        }
    }

    enum ApiErrorResponseMutatorStages {
        INSTANCE;

        public MutatorStage<ApiErrorResponse> reason(final String reason) {
            return MutatorStage.of(
                reason,
                (it, v) -> new ApiErrorResponse(v)
            );
        }
    }
}