package com.heyrudy.mybatissample.domain.spi.config;

import org.jooq.DSLContext;

public enum CriticalDSLContextKey
    implements CriticalConfigKey<DSLContext> {
    INSTANCE;

    @Override
    public Class<DSLContext> getType() {
        return DSLContext.class;
    }

    @Override
    public String toString() {
        return "CriticalDSLContextKey{}";
    }
}