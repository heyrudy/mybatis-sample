package com.heyrudy.mybatissample.domain;

import io.vavr.Function2;
import io.vavr.Function3;
import java.util.function.UnaryOperator;

public interface UtilsModule {

    @FunctionalInterface
    interface MutatorStage<STATE>
        extends UnaryOperator<STATE> {

        static <VALUE, STATE> MutatorStage<STATE> of(
            VALUE value, Function2<STATE, VALUE, STATE> mutator) {
            return input -> mutator.apply(input, value);
        }

        static <VALUE, STATE> MutatorStage<STATE> of2(
            VALUE value1, VALUE value2, Function3<STATE, VALUE, VALUE, STATE> mutator) {
            return input -> mutator.apply(input, value1, value2);
        }
    }
}