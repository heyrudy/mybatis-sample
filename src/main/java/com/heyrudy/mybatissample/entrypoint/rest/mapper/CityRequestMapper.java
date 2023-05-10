package com.heyrudy.mybatissample.entrypoint.rest.mapper;

import com.heyrudy.mybatissample.entrypoint.rest.dto.CityRequestDTO;
import com.heyrudy.mybatissample.entrypoint.utils.Mapper;
import com.heyrudy.mybatissample.core.model.city.City;
import org.springframework.stereotype.Component;

@Component
public class CityRequestMapper implements Mapper<City, CityRequestDTO> {
    public City toEntity(CityRequestDTO dto) {
        return City.builder()
                .name(dto.name())
                .state(dto.state())
                .country(dto.country())
                .build();
    }

    public CityRequestDTO toDto(City city) {
        return CityRequestDTO.builder()
                .name(city.getName())
                .state(city.getState())
                .country(city.getCountry())
                .build();
    }
}
