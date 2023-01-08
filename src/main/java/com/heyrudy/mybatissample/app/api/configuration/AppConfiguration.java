package com.heyrudy.mybatissample.app.api.configuration;

import com.heyrudy.mybatissample.app.api.internal.handlers.dbstorage.spring.entity.dto.mapper.CityMapper;
import com.heyrudy.mybatissample.app.api.internal.handlers.dbstorage.spring.entity.dto.mapper.CityMapperImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

    @Bean
    public CityMapper mapper() {
        return new CityMapperImpl();
    }
}
