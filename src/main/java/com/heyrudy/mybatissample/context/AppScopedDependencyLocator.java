package com.heyrudy.mybatissample.context;

import io.vavr.control.Option;
import java.util.function.Supplier;

public non-sealed interface AppScopedDependencyLocator
    extends Environment {

    <T> T getBeanOrMock(Class<T> beanClass, Option<Supplier<T>> fallback);

    class ErrorMessage {

        public static final String NO_CRITICAL_DB_SECRET_PROPERTIES_FOUND_FOR_KEY_ERROR_MESSAGE =
            "No critical db secret properties found for key: %s";
        public static final String NO_CRITICAL_DSL_CONTEXT_CONFIG_FOUND_FOR_KEY_ERROR_MESSAGE =
            "No critical dsl context config found for key: %s";
        public static final String NO_CRITICAL_REPOSITORY_FOUND_FOR_KEY_ERROR_MESSAGE =
            "No critical repository found for key: %s";
    }
}