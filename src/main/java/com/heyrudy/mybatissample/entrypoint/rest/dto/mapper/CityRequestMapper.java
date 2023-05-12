package com.heyrudy.mybatissample.entrypoint.rest.dto.mapper;

import com.heyrudy.mybatissample.core.model.city.City;
import com.heyrudy.mybatissample.entrypoint.rest.dto.CityRequestDTO;

public class CityRequestMapper {

    public static final CityRequestMapper CITY_RESQUEST_MAPPER = new CityRequestMapper();

    public City toModel(CityRequestDTO dto) {
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
