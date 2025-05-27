package com.heyrudy.mybatissample.context;

public sealed interface CriticalConfigKey<T>
    extends ConfigKey<T>
    permits CriticalPostgresDSLContextConfigKey, CriticalH2DSLContextConfigKey {

}