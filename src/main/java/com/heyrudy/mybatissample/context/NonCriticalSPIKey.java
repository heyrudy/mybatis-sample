package com.heyrudy.mybatissample.context;

sealed interface NonCriticalSPIKey<T>
    extends DependencyKey<T>
    permits AuditSPIKey {

}