package com.heyrudy.mybatissample.domain.spi.config;

import com.heyrudy.mybatissample.domain.model.error.CriticalRepositoryNotFoundByLocatorError;
import com.heyrudy.mybatissample.domain.model.error.DbCriticalServiceNotFoundByLocatorError;
import com.heyrudy.mybatissample.domain.spi.config.ServiceKey.CriticalRepositoryKey;
import com.heyrudy.mybatissample.domain.spi.config.ServiceKey.DbCriticalServiceKey;
import io.vavr.control.Either;
import io.vavr.control.Option;

public interface AppScopedLocator {

    <T> Either<CriticalRepositoryNotFoundByLocatorError, T> getCriticalRepository(
        CriticalRepositoryKey<T> key);

    <T> Either<DbCriticalServiceNotFoundByLocatorError, T> getDbCriticalService(
        DbCriticalServiceKey<T> key);

    <T> Option<T> getService(ServiceKey<T> key);

    boolean hasService(ServiceKey<?> key);

    class ErrorMessage {

        public static final String NO_DB_SPI_CRITICAL_SERVICE_FOUND_FOR_KEY_ERROR_MESSAGE =
            "No DB SPI critical service found for key: %s";
        public static final String NO_CRITICAL_REPOSITORY_FOUND_FOR_KEY_ERROR_MESSAGE =
            "No critical repository found for key: %s";
    }
}