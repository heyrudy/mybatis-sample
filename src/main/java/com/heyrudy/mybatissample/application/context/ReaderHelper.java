package com.heyrudy.mybatissample.application.context;

import cyclops.control.Reader;
import io.vavr.control.Either;
import java.util.function.Function;

public enum ReaderHelper {
    INSTANCE;

    private static <ENV, ERROR, B> Reader<
        ENV, Either<ERROR, B>
        > failure(ERROR error) {
        return _ ->
            Either.left(error);
    }

    <ENV, ERROR, A, B> Reader<
        ENV,
        Either<ERROR, B>
        > flatMapEither(
        Reader<ENV, Either<ERROR, A>> source,
        Function<A, Reader<ENV, Either<ERROR, B>>> next) {
        return source.flatMap(result ->
            result.fold(
                ReaderHelper::failure,
                next
            )
        );
    }
}
