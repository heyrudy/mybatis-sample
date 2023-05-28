package com.heyrudy.mybatissample.domain.api.config;

import com.heyrudy.mybatissample.domain.spi.ICityDbSPI;
import com.heyrudy.mybatissample.domain.interactor.CreateCity;
import com.heyrudy.mybatissample.domain.interactor.FindCities;
import com.heyrudy.mybatissample.domain.interactor.FindCityById;
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
