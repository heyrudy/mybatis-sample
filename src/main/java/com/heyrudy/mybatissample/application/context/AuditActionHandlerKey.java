package com.heyrudy.mybatissample.application.context;

import com.heyrudy.mybatissample.domain.CityProgramModule.AuditAction;
import com.heyrudy.mybatissample.domain.DomainErrorModule;
import com.heyrudy.mybatissample.domain.DomainErrorModule.MissingCriticalDependencyError;
import com.heyrudy.mybatissample.domain.DomainErrorModule.MissingCriticalProgramHandlerError;
import cyclops.control.Reader;
import io.vavr.control.Either;

public enum AuditActionHandlerKey
    implements ProgramHandlerKey<AuditAction<?, ?>, Void> {
    INSTANCE;

    private static final ProgramHandler<
        AuditAction<?, ?>, Void
        > AUDIT_ACTION_HANDLER =
        auditAction ->
            ProgramHandlerKey.resolve(
                    AuditSPIKey.INSTANCE, AuditActionHandlerKey::toAuditHandlerError)
                .flatMap(auditEither ->
                    auditEither.fold(
                        ProgramHandlerKey::failure,
                        audit -> {
                            audit.auditAction(
                                auditAction.context()
                            );

                            return _ ->
                                Either.right(null);
                        }
                    ));

    @Override
    public Reader<
        AppScopedDependencyLocator,
        Either<DomainErrorModule.MissingCriticalProgramHandlerError, ProgramHandler<AuditAction<?, ?>, Void>>
        > lazyLoad() {
        return _ ->
            Either.right(AUDIT_ACTION_HANDLER);
    }

    private static MissingCriticalProgramHandlerError
    toAuditHandlerError(
        MissingCriticalDependencyError error) {
        return new MissingCriticalProgramHandlerError.MissingNonCriticalAuditProgramHandlerError(
            error.message()
        );
    }
}