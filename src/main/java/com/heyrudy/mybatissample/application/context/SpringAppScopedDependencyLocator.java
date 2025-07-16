package com.heyrudy.mybatissample.application.context;

import io.vavr.control.Option;
import java.util.function.Supplier;
import org.springframework.context.ApplicationContext;

public record SpringAppScopedDependencyLocator(ApplicationContext applicationContext)
    implements AppScopedDependencyLocator {

    @Override
    public <T> T getDependency(Class<T> dependencyClass, Option<Supplier<T>> fallback) {
        return applicationContext
            .getBeanProvider(dependencyClass)
            .getIfAvailable(() -> fallback.map(Supplier::get).getOrNull());
    }
}