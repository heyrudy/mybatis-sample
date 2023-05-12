package com.heyrudy.mybatissample.entrypoint.rest.mapper;

import com.heyrudy.mybatissample.entrypoint.rest.dto.CityResponseDTO;
import com.heyrudy.mybatissample.core.model.city.City;

public class CityResponseMapper {

    public static final CityResponseMapper CITY_RESPONSE_MAPPER = new CityResponseMapper();

    public City toModel(CityResponseDTO dto) {
        return City.builder()
                .name(dto.name())
                .state(dto.state())
                .country(dto.country())
                .build();
    }

    public CityResponseDTO toDto(City entity) {
        return CityResponseDTO.builder()
                .name(entity.getName())
                .state(entity.getState())
                .country(entity.getCountry())
                .build();
    }
}
