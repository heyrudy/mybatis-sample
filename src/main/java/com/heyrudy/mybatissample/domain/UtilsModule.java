package com.heyrudy.mybatissample.domain;

import io.vavr.Function2;
import io.vavr.Function3;

public interface UtilsModule {

    interface MutatorOption<STATE> {

        STATE apply(STATE input);

        static <VALUE, STATE> MutatorOption<STATE> of(
            VALUE value, Function2<STATE, VALUE, STATE> mutator) {
            return input -> mutator.apply(input, value);
        }

        static <VALUE, STATE> MutatorOption<STATE> of2(
            VALUE value1, VALUE value2, Function3<STATE, VALUE, VALUE, STATE> mutator) {
            return input -> mutator.apply(input, value1, value2);
        }
    }
}
