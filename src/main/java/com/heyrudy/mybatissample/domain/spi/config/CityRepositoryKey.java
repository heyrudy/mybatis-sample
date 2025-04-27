package com.heyrudy.mybatissample.domain.spi.config;

import com.heyrudy.mybatissample.domain.spi.ICityRepository;

public enum CityRepositoryKey implements CriticalRepositoryKey<ICityRepository> {
    INSTANCE;

    @Override
    public Class<ICityRepository> getType() {
        return ICityRepository.class;
    }

    @Override
    public String toString() {
        return "CityRepositoryKey{}";
    }
}