package com.heyrudy.mybatissample.domain.spi.config;

sealed interface AuditSPIServiceKey<T>
    extends ServiceKey<T>
    permits AuditSPIKey {

}