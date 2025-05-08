package com.heyrudy.mybatissample.context;

import com.heyrudy.mybatissample.context.Environment.NoEnv;

public sealed interface Environment
    permits AppScopedDependencyLocator,
    NoEnv {

    record NoEnv() implements Environment {

    }
}
