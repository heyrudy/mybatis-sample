package com.heyrudy.mybatissample.domain.spi.config;

import javax.sql.DataSource;

public enum CriticalDataSourceKey
    implements CriticalConfigKey<DataSource> {
    INSTANCE;

    @Override
    public Class<DataSource> getType() {
        return DataSource.class;
    }

    @Override
    public String toString() {
        return "CriticalDataSourceKey{}";
    }
}