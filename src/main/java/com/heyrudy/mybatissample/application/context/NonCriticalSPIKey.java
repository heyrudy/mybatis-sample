package com.heyrudy.mybatissample.application.context;

sealed interface NonCriticalSPIKey<T>
    extends DependencyKey<T>
    permits AuditSPIKey {

}
