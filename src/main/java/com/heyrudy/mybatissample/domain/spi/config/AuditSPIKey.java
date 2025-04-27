package com.heyrudy.mybatissample.domain.spi.config;

import com.heyrudy.mybatissample.domain.spi.IAuditSPI;

public enum AuditSPIKey implements AuditSPIServiceKey<IAuditSPI> {
    INSTANCE;

    @Override
    public Class<IAuditSPI> getType() {
        return IAuditSPI.class;
    }

    @Override
    public String toString() {
        return "AuditSPIKey{}";
    }
}