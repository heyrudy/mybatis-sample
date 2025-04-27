package com.heyrudy.mybatissample.domain.spi.config;

import com.heyrudy.mybatissample.gateway.config.IDbSecretProperties;

public enum CriticalDbSecretPropertiesKey
    implements CriticalSecretKey<IDbSecretProperties> {
    INSTANCE;

    @Override
    public Class<IDbSecretProperties> getType() {
        return IDbSecretProperties.class;
    }

    @Override
    public String toString() {
        return "CriticalDatabasePropertiesKey{}";
    }
}