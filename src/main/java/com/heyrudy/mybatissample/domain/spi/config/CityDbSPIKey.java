package com.heyrudy.mybatissample.domain.spi.config;

import com.heyrudy.mybatissample.domain.spi.ICityDbSPI;

public enum CityDbSPIKey implements DbCriticalServiceKey<ICityDbSPI> {
    INSTANCE;

    @Override
    public Class<ICityDbSPI> getType() {
        return ICityDbSPI.class;
    }

    @Override
    public String toString() {
        return "CityDbKey{}";
    }
}