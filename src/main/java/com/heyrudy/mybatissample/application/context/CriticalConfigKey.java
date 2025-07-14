package com.heyrudy.mybatissample.application.context;

public sealed interface CriticalConfigKey<T>
    extends ConfigKey<T>
    permits CriticalPostgresDSLContextConfigKey, CriticalH2DSLContextConfigKey {

}