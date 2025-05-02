package com.heyrudy.mybatissample.domain.spi.config;

import com.heyrudy.mybatissample.domain.spi.config.Environment.NoEnv;

public sealed interface Environment
    permits AppScopedDependencyLocator,
    NoEnv {

    record NoEnv() implements Environment {

    }
}
