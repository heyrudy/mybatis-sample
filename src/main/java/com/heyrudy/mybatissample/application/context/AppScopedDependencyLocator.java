package com.heyrudy.mybatissample.application.context;

import io.vavr.control.Option;
import java.util.function.Supplier;

@FunctionalInterface
public non-sealed interface AppScopedDependencyLocator
    extends Environment {

    <T> T getDependency(Class<T> dependencyClass, Option<Supplier<T>> fallback);
}
