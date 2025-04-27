package com.heyrudy.mybatissample.domain.spi.config;

import com.heyrudy.mybatissample.domain.model.error.CriticalRepositoryNotFoundByServiceLocatorError;
import com.heyrudy.mybatissample.domain.model.error.DbCriticalServiceNotFoundByServiceLocatorError;
import com.heyrudy.mybatissample.domain.model.error.MissingCriticalConfigError;
import io.vavr.control.Either;
import io.vavr.control.Option;

public interface AppScopedServiceLocator {

    <T> Either<MissingCriticalConfigError, T> getCriticalConfig(
        CriticalConfigLocatorKey<T> key);

    <T> Either<CriticalRepositoryNotFoundByServiceLocatorError, T> getCriticalRepository(
        CriticalRepositoryKey<T> key);

    <T> Either<DbCriticalServiceNotFoundByServiceLocatorError, T> getDbCriticalService(
        DbCriticalServiceKey<T> key);

    <T> Option<T> getService(ServiceKey<T> key);

    boolean hasService(ServiceKey<?> key);

    class ErrorMessage {

        public static final String NO_CRITICAL_REPOSITORY_FOUND_FOR_KEY_ERROR_MESSAGE =
            "No critical repository found for key: %s";
        public static final String NO_DB_SPI_CRITICAL_SERVICE_FOUND_FOR_KEY_ERROR_MESSAGE =
            "No DB SPI critical service found for key: %s";
    }
}