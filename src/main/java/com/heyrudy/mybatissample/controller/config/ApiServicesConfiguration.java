package com.heyrudy.mybatissample.controller.config;

import com.heyrudy.mybatissample.domain.api.CreateCityInteractorAPI;
import com.heyrudy.mybatissample.domain.api.FindCitiesInteractorAPI;
import com.heyrudy.mybatissample.domain.api.FindCityByIdInteractorAPI;
import com.heyrudy.mybatissample.domain.spi.ICityDbSPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApiServicesConfiguration {

    @Bean
    public CreateCityInteractorAPI createCityInteractorAPI(ICityDbSPI db) {
        return new CreateCityInteractorAPI(db);
    }

    @Bean
    public FindCityByIdInteractorAPI findCityByIdInteractorAPI(ICityDbSPI db) {
        return new FindCityByIdInteractorAPI(db);
    }

    @Bean
    public FindCitiesInteractorAPI findCitiesInteractorAPI(ICityDbSPI db) {
        return new FindCitiesInteractorAPI(db);
    }

}
