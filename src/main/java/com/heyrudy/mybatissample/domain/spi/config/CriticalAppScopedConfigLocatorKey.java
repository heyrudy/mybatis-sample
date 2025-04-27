package com.heyrudy.mybatissample.domain.spi.config;

import com.heyrudy.mybatissample.gateway.config.AppScopedConfigLocator;

public enum CriticalAppScopedConfigLocatorKey
    implements CriticalConfigLocatorKey<AppScopedConfigLocator> {
    INSTANCE;

    @Override
    public Class<AppScopedConfigLocator> getType() {
        return AppScopedConfigLocator.class;
    }

    @Override
    public String toString() {
        return "CriticalAppScopedConfigLocatorKey{}";
    }
}
