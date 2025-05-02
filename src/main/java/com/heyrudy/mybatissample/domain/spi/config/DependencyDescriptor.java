package com.heyrudy.mybatissample.domain.spi.config;

import com.heyrudy.mybatissample.domain.model.error.MissingCriticalDependencyError;
import io.vavr.control.Option;

public class DependencyDescriptor<T, E extends MissingCriticalDependencyError> {

    private final T service;
    private final Option<E> missingCriticalDependencyError;

    private DependencyDescriptor(
        T service,
        Option<E> missingCriticalDependencyError) {
        this.service = service;
        this.missingCriticalDependencyError = missingCriticalDependencyError;
    }

    /**
     * Creates a new DependencyDescriptor.
     *
     * @param service The service instance
     * @param missingCriticalDependencyError Function to create appropriate error
     * @param <T> Type of service
     * @param <E> Type of error
     * @return A new DependencyDescriptor
     */
    public static <T, E extends MissingCriticalDependencyError> DependencyDescriptor<T, E> of(
        T service, Option<E> missingCriticalDependencyError) {
        return new DependencyDescriptor<>(service, missingCriticalDependencyError);
    }

    public T getService() {
        return service;
    }

    public Option<E> getMissingCriticalDependencyError() {
        return missingCriticalDependencyError;
    }
}