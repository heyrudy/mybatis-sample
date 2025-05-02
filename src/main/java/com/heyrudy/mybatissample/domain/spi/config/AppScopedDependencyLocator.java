package com.heyrudy.mybatissample.domain.spi.config;

import static cyclops.function.Predicates.instanceOf;
import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;

import com.heyrudy.mybatissample.domain.model.error.CriticalDSLContextNotFoundByConfigLocatorError;
import com.heyrudy.mybatissample.domain.model.error.CriticalDbSecretPropertiesNotFoundBySecretLocatorError;
import com.heyrudy.mybatissample.domain.model.error.CriticalRepositoryNotFoundByServiceLocatorError;
import com.heyrudy.mybatissample.domain.model.error.DbCriticalServiceNotFoundByServiceLocatorError;
import com.heyrudy.mybatissample.domain.model.error.MissingCriticalDependencyError;
import io.vavr.control.Either;
import io.vavr.control.Option;
import java.util.function.Function;

public non-sealed interface AppScopedDependencyLocator
    extends Environment {

    <T> Either<MissingCriticalDependencyError, T> getDependency(DependencyKey<T> key);

    class ErrorMessage {

        public static final String NO_CRITICAL_DB_SECRET_PROPERTIES_FOUND_FOR_KEY_ERROR_MESSAGE =
            "No critical db secret properties found for key: %s";

        public static final String NO_CRITICAL_DSL_CONTEXT_CONFIG_FOUND_FOR_KEY_ERROR_MESSAGE =
            "No critical dsl context config found for key: %s";

        public static final String NO_CRITICAL_REPOSITORY_FOUND_FOR_KEY_ERROR_MESSAGE =
            "No critical repository found for key: %s";
        public static final String NO_DB_SPI_CRITICAL_SERVICE_FOUND_FOR_KEY_ERROR_MESSAGE =
            "No DB SPI critical service found for key: %s";

        public static Function<EnvironmentKey<?>, Option<? extends MissingCriticalDependencyError>> toDependencyError() {
            return key ->
                Match(key).of(
                    Case($(instanceOf(SecretKey.class)),
                        it -> Option.of(new CriticalDbSecretPropertiesNotFoundBySecretLocatorError(
                            AppScopedDependencyLocator.ErrorMessage.NO_CRITICAL_DB_SECRET_PROPERTIES_FOUND_FOR_KEY_ERROR_MESSAGE
                                .formatted(it)))),
                    Case($(instanceOf(ConfigKey.class)),
                        it -> Option.of(new CriticalDSLContextNotFoundByConfigLocatorError(
                            AppScopedDependencyLocator.ErrorMessage.NO_CRITICAL_DSL_CONTEXT_CONFIG_FOUND_FOR_KEY_ERROR_MESSAGE
                                .formatted(it)))),
                    Case($(instanceOf(CriticalRepositoryKey.class)),
                        it -> Option.of(new CriticalRepositoryNotFoundByServiceLocatorError(
                            AppScopedDependencyLocator.ErrorMessage.NO_CRITICAL_REPOSITORY_FOUND_FOR_KEY_ERROR_MESSAGE
                                .formatted(it)))),
                    Case($(instanceOf(DependencyKey.class)),
                        it -> Option.of(new DbCriticalServiceNotFoundByServiceLocatorError(
                            AppScopedDependencyLocator.ErrorMessage.NO_DB_SPI_CRITICAL_SERVICE_FOUND_FOR_KEY_ERROR_MESSAGE
                                .formatted(it)))),
                    Case($(), () -> Option.none())
                );
        }
    }
}