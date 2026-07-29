package com.heyrudy.mybatissample.domain;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Patterns.$None;
import static io.vavr.Patterns.$Some;

import com.heyrudy.mybatissample.application.context.AppScopedDependencyLocator;
import com.heyrudy.mybatissample.application.context.AuditAction;
import com.heyrudy.mybatissample.application.context.AuditActionHandlerKey;
import com.heyrudy.mybatissample.application.context.CityContextModule;
import com.heyrudy.mybatissample.gateway.AuditModule;
import cyclops.control.Reader;
import io.vavr.control.Either;
import java.util.List;
import java.util.function.Function;

public interface CityProgramModule
    extends CityContextModule
    , CityModelModule
    , DomainErrorModule
    , AuditModule {

    record Program<A>(
        Reader<
            CityProgramASTInterpreter,
            Reader<
                AppScopedDependencyLocator,
                Either<DomainError, A>
                >
            > computation
    ) {

        public static <A> Program<A> pure(
            A value) {
            return new Program<>(
                _ ->
                    _ ->
                        Either.right(
                            value
                        )
            );
        }

        public <B> Program<B> map(
            Function<A, B> mapper) {
            return flatMap(
                value ->
                    pure(
                        mapper.apply(value)
                    )
            );
        }

        public <B> Program<B> flatMap(
            Function<A, Program<B>> continuation) {
            return new Program<>(
                computation.flatMap(
                    currentProgram ->
                        interpreter ->
                            currentProgram.flatMap(
                                resultEither ->
                                    resultEither.fold(
                                        error ->
                                            _ ->
                                                Either.left(error),
                                        value ->
                                            continuation
                                                .apply(value)
                                                .computation()
                                                .apply(interpreter)
                                    )
                            )
                )
            );
        }

        public static Program<ICity> save(
            ICity city) {
            return new Program<>(
                interpreter ->
                    interpreter.save(
                        city));
        }

        public static Program<ICity> find(
            Long cityId) {
            return new Program<>(
                interpreter ->
                    interpreter.find(
                        cityId));
        }

        public static Program<List<ICity>> findAll() {
            return new Program<>(
                CityProgramASTInterpreter::findAll);
        }

        public static Program<Void> audit(
            AuditContext<?, ?> context) {
            return new Program<>(
                interpreter ->
                    interpreter.audit(
                        context
                    )
            );
        }

        public Program<A> withAudit(
            Class<?> caller) {
            return Program
                .audit(
                    AuditContext.of(
                        AuditContextMutatorStages.INSTANCE.phase(
                            Phase.START),
                        AuditContextMutatorStages.INSTANCE.caller(
                            caller)))
                .flatMap(
                    _ ->
                        this.flatMap(
                            result ->
                                Program.audit(
                                        AuditContext.of(
                                            AuditContextMutatorStages.INSTANCE.phase(
                                                Phase.END),
                                            AuditContextMutatorStages.INSTANCE.caller(
                                                caller),
                                            AuditContextMutatorStages.INSTANCE.outcome(
                                                Either.right(result))))
                                    .map(
                                        _ -> result
                                    )
                        )
                );
        }
    }

    enum CreateCityWorkflow {
        INSTANCE;

        public Program<ICity> execute(ICity city) {
            return Program
                .save(city)
                .flatMap(Program::pure)
                .withAudit(CreateCityWorkflow.class);
        }
    }

    enum FindCityByIdWorkflow {
        INSTANCE;

        public Program<ICity> execute(Long cityId) {
            return Program
                .find(cityId)
                .flatMap(Program::pure)
                .withAudit(FindCityByIdWorkflow.class);
        }
    }

    enum FindCitiesWorkflow {
        INSTANCE;

        public Program<List<ICity>> execute() {
            return Program
                .findAll()
                .flatMap(Program::pure)
                .withAudit(FindCitiesWorkflow.class);
        }
    }

    interface CityProgramASTInterpreter {

        Reader<
            AppScopedDependencyLocator,
            Either<DomainError, ICity>
            > save(ICity city);

        Reader<
            AppScopedDependencyLocator,
            Either<DomainError, ICity>
            > find(Long cityId);

        Reader<
            AppScopedDependencyLocator,
            Either<DomainError, List<ICity>>
            > findAll();

        Reader<
            AppScopedDependencyLocator,
            Either<DomainError, Void>
            > audit(AuditContext<?, ?> context);
    }

    enum ProductionInterpreter
        implements CityProgramASTInterpreter {
        INSTANCE;

        @Override
        public Reader<
            AppScopedDependencyLocator,
            Either<DomainError, ICity>
            > save(ICity city) {
            return SaveCityHandlerKey.INSTANCE.lazyLoad()
                .flatMap(
                    handlerEither ->
                        handlerEither.fold(
                            ProductionInterpreter::failure,
                            handler ->
                                handler.handle(
                                    new SaveCity(city))
                        )
                );
        }

        @Override
        public Reader<
            AppScopedDependencyLocator,
            Either<DomainError, ICity>
            > find(Long cityId) {
            return FindCityHandlerKey.INSTANCE.lazyLoad()
                .flatMap(
                    handlerEither ->
                        handlerEither.fold(
                            ProductionInterpreter::failure,
                            handler ->
                                handler.handle(
                                        new FindCity(cityId))
                                    .map(optionEither ->
                                        optionEither.flatMap(iCityOption ->
                                            Match(iCityOption).of(
                                                Case($Some($()), Either::right),
                                                Case($None(), () -> Either.left(
                                                    new DomainErrorModule.DomainServiceAPIError.CityNotFoundError(
                                                        DomainErrorModule.DomainServiceAPIError.CityNotFoundError.ErrorMessage.CITY_NOT_FOUND
                                                            .formatted(cityId)))))))
                        )
                );
        }

        @Override
        public Reader<
            AppScopedDependencyLocator,
            Either<DomainError, List<ICity>>
            > findAll() {
            return FindCitiesHandlerKey.INSTANCE.lazyLoad()
                .flatMap(
                    handlerEither ->
                        handlerEither.fold(
                            ProductionInterpreter::failure,
                            handler ->
                                handler.handle(
                                    new FindCities())
                        )
                );
        }

        @Override
        public Reader<
            AppScopedDependencyLocator,
            Either<DomainError, Void>
            > audit(AuditContext<?, ?> context) {
            return AuditActionHandlerKey.INSTANCE.lazyLoad()
                .flatMap(
                    handlerEither ->
                        handlerEither.fold(
                            ProductionInterpreter::failure,
                            handler ->
                                handler.handle(
                                    new AuditAction<>(context))
                        )
                );
        }

        private static <A>
        Reader<
            AppScopedDependencyLocator,
            Either<DomainError, A>
            > failure(DomainError error) {
            return _ ->
                Either.left(error);
        }
    }

    enum CityRuntime {
        INSTANCE;

        public <A>
        Reader<
            AppScopedDependencyLocator,
            Either<DomainError, A>
            > run(Program<A> program) {
            return program
                .computation()
                .apply(ProductionInterpreter.INSTANCE);
        }
    }
}
