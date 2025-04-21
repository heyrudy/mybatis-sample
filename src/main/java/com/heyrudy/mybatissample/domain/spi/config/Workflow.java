package com.heyrudy.mybatissample.domain.spi.config;

import cyclops.control.Reader;
import io.vavr.control.Either;

public interface Workflow<ENV, ERR, T> extends Reader<ENV, Either<ERR, T>> {

}
