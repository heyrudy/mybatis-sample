package com.heyrudy.mybatissample.domain.spi.config;

sealed interface NonCriticalSPIKey<T>
    extends DependencyKey<T>
    permits AuditSPIKey {

}