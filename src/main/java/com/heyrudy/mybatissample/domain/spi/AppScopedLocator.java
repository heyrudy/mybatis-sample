package com.heyrudy.mybatissample.domain.spi;

import com.heyrudy.mybatissample.domain.spi.ServiceKey.DbServiceKey;
import com.heyrudy.mybatissample.domain.model.error.DBServiceNotFoundByLocatorError;
import io.vavr.control.Either;
import io.vavr.control.Option;

public interface AppScopedLocator {

    <T> Either<DBServiceNotFoundByLocatorError, T> getDBService(DbServiceKey<T> key);

    <T> Option<T> getService(ServiceKey<T> key);

    boolean hasService(ServiceKey<?> key);
}