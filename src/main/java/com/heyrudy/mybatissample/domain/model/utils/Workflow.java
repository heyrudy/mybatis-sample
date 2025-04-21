package com.heyrudy.mybatissample.domain.model.utils;

import cyclops.control.Reader;
import io.vavr.control.Either;

public interface Workflow<ENV, ERR, T> extends Reader<ENV, Either<ERR, T>> {

}
