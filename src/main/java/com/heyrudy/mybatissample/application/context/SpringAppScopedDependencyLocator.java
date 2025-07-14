package com.heyrudy.mybatissample.application.context;

import io.vavr.control.Option;
import java.util.function.Supplier;
import org.springframework.context.ApplicationContext;

public class SpringAppScopedDependencyLocator
    implements AppScopedDependencyLocator {

    private final ApplicationContext applicationContext;

    public SpringAppScopedDependencyLocator(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public <T> T getDependency(Class<T> dependencyClass, Option<Supplier<T>> fallback) {
        return applicationContext
            .getBeanProvider(dependencyClass)
            .getIfAvailable(() -> fallback.map(Supplier::get).getOrNull());
    }
}