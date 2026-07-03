package com.heyrudy.mybatissample.application.context;

import com.heyrudy.mybatissample.domain.CityProgramModule.CityProgramAST;
import com.heyrudy.mybatissample.domain.DomainErrorModule.DomainError;
import cyclops.control.Reader;
import io.vavr.control.Either;

@FunctionalInterface
public interface ProgramHandler<P extends CityProgramAST, R> {

    Reader<
        AppScopedDependencyLocator,
        Either<DomainError, R>
        > handle(P program);
}