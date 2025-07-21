package com.heyrudy.mybatissample.domain;

import java.util.function.BiFunction;

public interface UtilsModule {

    interface MutatorOption<STATE> {

        STATE apply(STATE input);

        static <VALUE, STATE> MutatorOption<STATE> of(
            VALUE value, BiFunction<STATE, VALUE, STATE> mutator) {
            return input -> mutator.apply(input, value);
        }
    }
}
