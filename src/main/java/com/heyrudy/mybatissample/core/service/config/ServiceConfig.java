package com.heyrudy.mybatissample.core.service.config;

import com.heyrudy.mybatissample.core.spi.store.ICityDbSPI;
import com.heyrudy.mybatissample.core.interactor.CreateCity;
import com.heyrudy.mybatissample.core.interactor.FindCities;
import com.heyrudy.mybatissample.core.interactor.FindCityById;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfig {

    @Bean
    public CreateCity createCity(ICityDbSPI store) {
        return new CreateCity(store);
    }

    @Bean
    public FindCityById findCityById(ICityDbSPI store) {
        return new FindCityById(store);
    }

    @Bean
    public FindCities findCities(ICityDbSPI store) {
        return new FindCities(store);
    }
}
