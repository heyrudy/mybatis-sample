package com.heyrudy.mybatissample.configuration;

import com.heyrudy.mybatissample.dto.mapper.CityMapper;
import com.heyrudy.mybatissample.dto.mapper.CityMapperImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

    @Bean
    public CityMapper mapper() {
        return new CityMapperImpl();
    }
}
