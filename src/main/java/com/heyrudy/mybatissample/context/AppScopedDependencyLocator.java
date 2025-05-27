package com.heyrudy.mybatissample.context;

import io.vavr.control.Option;
import java.util.function.Supplier;

@FunctionalInterface
public non-sealed interface AppScopedDependencyLocator
    extends Environment {

    <T> T getDependency(Class<T> beanClass, Option<Supplier<T>> fallback);
}