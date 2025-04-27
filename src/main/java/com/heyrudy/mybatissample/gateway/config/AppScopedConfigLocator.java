package com.heyrudy.mybatissample.gateway.config;

import com.heyrudy.mybatissample.domain.model.error.CriticalDSLContextNotFoundByConfigLocatorError;
import com.heyrudy.mybatissample.domain.model.error.CriticalDataSourceNotFoundByConfigLocatorError;
import com.heyrudy.mybatissample.domain.spi.config.ConfigKey;
import com.heyrudy.mybatissample.domain.spi.config.CriticalConfigKey;
import io.vavr.control.Either;
import io.vavr.control.Option;

public interface AppScopedConfigLocator {

    <T> Either<CriticalDataSourceNotFoundByConfigLocatorError, T> getCriticalDataSourceConfig(
        CriticalConfigKey<T> key);

    <T> Either<CriticalDSLContextNotFoundByConfigLocatorError, T> getCriticalDSLContextConfig(
        CriticalConfigKey<T> key);

    <T> Option<T> getConfig(ConfigKey<T> key);

    boolean hasConfig(ConfigKey<?> key);

    class ErrorMessage {

        public static final String NO_CRITICAL_DATA_SOURCE_CONFIG_FOUND_FOR_KEY_ERROR_MESSAGE =
            "No critical data source config found for key: %s";
        public static final String NO_CRITICAL_DSL_CONTEXT_CONFIG_FOUND_FOR_KEY_ERROR_MESSAGE =
            "No critical dsl context config found for key: %s";
    }
}