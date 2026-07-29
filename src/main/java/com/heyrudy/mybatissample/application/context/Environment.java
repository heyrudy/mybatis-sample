package com.heyrudy.mybatissample.application.context;

public sealed interface Environment
    permits AppScopedDependencyLocator
    , Environment.NoEnv {

    record NoEnv() implements Environment {

    }
}
