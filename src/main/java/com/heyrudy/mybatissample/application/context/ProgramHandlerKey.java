package com.heyrudy.mybatissample.application.context;

import com.heyrudy.mybatissample.application.context.CityContextModule.CityProgramAST;
import com.heyrudy.mybatissample.application.context.CityContextModule.CityProgramHandlerKey;
import com.heyrudy.mybatissample.domain.DomainErrorModule;
import com.heyrudy.mybatissample.domain.DomainErrorModule.DomainError;
import com.heyrudy.mybatissample.domain.DomainErrorModule.MissingCriticalDependencyError;
import com.heyrudy.mybatissample.domain.DomainErrorModule.MissingCriticalProgramHandlerError;
import cyclops.control.Reader;
import io.vavr.control.Either;
import java.util.function.Function;

public sealed interface ProgramHandlerKey<P extends CityProgramAST, R>
    extends CapabilityKey
    permits CityProgramHandlerKey
    , AuditActionHandlerKey {

    Reader<
        AppScopedDependencyLocator,
        Either<DomainErrorModule.MissingCriticalProgramHandlerError, CityContextModule.CityProgramHandler<P, R>>
        > lazyLoad();

    static <E, A>
    Reader<
        AppScopedDependencyLocator,
        Either<E, A>
        > failure(E error) {
        return _ ->
            Either.left(error);
    }

    static <T>
    Reader<
        AppScopedDependencyLocator,
        Either<DomainErrorModule.MissingCriticalProgramHandlerError, T>
        > resolve(
        DependencyKey<T> key,
        Function<MissingCriticalDependencyError, DomainErrorModule.MissingCriticalProgramHandlerError> mapper) {
        return key.lazyLoad()
            .map(
                either ->
                    either.mapLeft(mapper)
            );
    }

    static <E extends DomainError, A>
    Reader<
        AppScopedDependencyLocator,
        Either<DomainError, A>
        > toDomainError(
        Reader<
            AppScopedDependencyLocator,
            Either<E, A>
            > reader) {
        return reader.map(
            either ->
                either.mapLeft(
                    error -> error
                )
        );
    }

    static <CAPABILITY, RESULT, E extends DomainError>
    Reader<
        AppScopedDependencyLocator,
        Either<DomainError, RESULT>
        > resolveAndThen(
        DependencyKey<CAPABILITY> key,
        Function<
            MissingCriticalDependencyError, MissingCriticalProgramHandlerError
            > mapper,
        Function<
            CAPABILITY,
            Reader<
                AppScopedDependencyLocator,
                Either<E, RESULT>
                >
            > next) {
        return resolve(key, mapper)
            .flatMap(
                capabilityEither ->
                    capabilityEither.fold(
                        ProgramHandlerKey::failure,
                        capability ->
                            ProgramHandlerKey.toDomainError(
                                next.apply(capability)
                            )
                    )
            );
    }
}