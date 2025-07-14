package com.heyrudy.mybatissample.application.context;

import com.heyrudy.mybatissample.application.context.Environment.NoEnv;

public sealed interface Environment
    permits AppScopedDependencyLocator,
    NoEnv {

    record NoEnv() implements Environment {

    }
}