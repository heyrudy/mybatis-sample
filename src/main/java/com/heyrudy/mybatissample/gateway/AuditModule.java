package com.heyrudy.mybatissample.gateway;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;

import com.heyrudy.mybatissample.domain.UtilsModule.MutatorStage;
import com.heyrudy.mybatissample.gateway.AuditModule.IAuditSPI.LoggerMessages;
import io.vavr.control.Either;
import io.vavr.control.Option;
import java.util.Objects;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

public interface AuditModule {

    enum Phase {
        START, IN_PROGRESS, END
    }

    record AuditContext<E, T>(
        Option<Either<E, T>> outcome,
        Function<E, String> errorMapper,
        Phase phase,
        Class<?> caller
    ) {

        private static <E, T> AuditContext<E, T> empty() {
            return new AuditContext<>(Option.none(), _ -> "", Phase.START, AuditContext.class);
        }

        @SafeVarargs
        public static <E, T> AuditContext<E, T> of(
            final MutatorStage<AuditContext<E, T>>... stages) {
            return Stream.of(stages)
                .filter(Objects::nonNull)
                .reduce(
                    AuditContext.empty(),
                    (acc, stage) -> stage.apply(acc),
                    (_, right) -> right
                );
        }

        Level resolvedLevel() {
            return outcome.fold(
                () -> java.util.logging.Level.INFO,
                either -> either.isRight()
                    ? java.util.logging.Level.INFO
                    : java.util.logging.Level.SEVERE
            );
        }

        String resolvedResult() {
            return outcome.fold(
                () -> LoggerMessages.ApiLoggerMessages.DEBUT_TRAITEMENT_API,
                either -> either.fold(
                    error -> "%s: %s".formatted(
                        LoggerMessages.ApiLoggerMessages.ERREUR, errorMapper.apply(error)),
                    value -> "SUCCESS: %s".formatted(
                        Option.of(value).map("%s"::formatted).getOrElse(""))
                )
            );
        }
    }

    enum AuditContextMutatorStages {
        INSTANCE;

        public <E, T> MutatorStage<AuditContext<E, T>> outcome(final Either<E, T> outcome) {
            return MutatorStage.of(
                outcome,
                (it, v) -> new AuditContext<>(Option.some(v), it.errorMapper, it.phase, it.caller)
            );
        }

        public <E, T> MutatorStage<AuditContext<E, T>> errorMapper(
            final Function<E, String> errorMapper) {
            return MutatorStage.of(
                errorMapper,
                (it, v) -> new AuditContext<>(it.outcome, v, it.phase, it.caller)
            );
        }

        public <E, T> MutatorStage<AuditContext<E, T>> phase(final Phase phase) {
            return MutatorStage.of(
                phase,
                (it, v) -> new AuditContext<>(it.outcome, it.errorMapper, v, it.caller)
            );
        }

        public <E, T> MutatorStage<AuditContext<E, T>> caller(final Class<?> caller) {
            return MutatorStage.of(
                caller,
                (it, v) -> new AuditContext<>(it.outcome, it.errorMapper, it.phase, v)
            );
        }
    }

    interface IAuditSPI {

        <E, T> void auditAction(AuditContext<E, T> context);

        interface LoggerMessages {

            class ApiLoggerMessages {

                // API execution phase messages
                public static final String DEBUT_TRAITEMENT_API = "[Début]: ";
                public static final String EN_COURS_TRAITEMENT_API = "[...]: ";
                public static final String FIN_TRAITEMENT_API = "[Fin]: ";

                // Error messages
                public static final String ERREUR = "Erreur";

                private ApiLoggerMessages() {
                    throw new AssertionError("This class cannot be instantiated");
                }
            }
        }
    }

    enum NoOpAuditAdapter
        implements IAuditSPI {
        INSTANCE;

        @Override
        public <E, T> void auditAction(final AuditContext<E, T> context) {
            // intentional no-op — audit disabled via config
        }
    }

    enum MockedAuditAdapter
        implements IAuditSPI {
        INSTANCE;

        @Override
        public <E, T> void auditAction(final AuditContext<E, T> context) {
            Logger logger = java.util.logging.Logger.getLogger(context.caller().getName());

            logger.log(
                context.resolvedLevel(),
                Match(context.phase()).of(
                    Case($(Phase.START), () ->
                        "%s%s".formatted(
                            LoggerMessages.ApiLoggerMessages.DEBUT_TRAITEMENT_API,
                            context.caller().getSimpleName())),
                    Case($(Phase.IN_PROGRESS), () ->
                        "%s%s".formatted(
                            LoggerMessages.ApiLoggerMessages.EN_COURS_TRAITEMENT_API,
                            context.caller().getSimpleName())),
                    Case($(Phase.END), () ->
                        "%s%s | result = %s".formatted(
                            LoggerMessages.ApiLoggerMessages.FIN_TRAITEMENT_API,
                            context.caller().getSimpleName(), context.resolvedResult()))));
        }
    }

    enum AuditAdapterResolver {
        INSTANCE;

        private static final String AUDIT_ENABLED_KEY = "audit.enabled";

        public IAuditSPI resolve() {
            String enabled = System.getProperty(AUDIT_ENABLED_KEY, "true");
            return enabled.equalsIgnoreCase("true")
                ? MockedAuditAdapter.INSTANCE
                : NoOpAuditAdapter.INSTANCE;
        }
    }
}