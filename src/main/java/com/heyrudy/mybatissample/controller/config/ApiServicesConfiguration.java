package com.heyrudy.mybatissample.controller.config;

import com.heyrudy.mybatissample.domain.api.CityServiceAPI;
import com.heyrudy.mybatissample.domain.service.CreateCity;
import com.heyrudy.mybatissample.domain.service.FindCities;
import com.heyrudy.mybatissample.domain.service.FindCityById;
import com.heyrudy.mybatissample.domain.spi.ICityDbSPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiServicesConfiguration {

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

    @Bean
    public CityServiceAPI cityServiceAPI(CreateCity createCity, FindCityById findCityById, FindCities findCities) {
        return new CityServiceAPI(createCity, findCityById, findCities);
    }

}
