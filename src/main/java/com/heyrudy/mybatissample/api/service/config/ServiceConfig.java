package com.heyrudy.mybatissample.api.service.config;

import com.heyrudy.mybatissample.core.spi.dbstorage.ICityStore;
import com.heyrudy.mybatissample.core.usecase.CreateCity;
import com.heyrudy.mybatissample.core.usecase.FindCities;
import com.heyrudy.mybatissample.core.usecase.FindCityById;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    @Bean
    public CreateCity createCity(ICityStore store) {
        return new CreateCity(store);
    }

    @Bean
    public FindCityById findCityById(ICityStore store) {
        return new FindCityById(store);
    }

    @Bean
    public FindCities findCities(ICityStore store) {
        return new FindCities(store);
    }
}
