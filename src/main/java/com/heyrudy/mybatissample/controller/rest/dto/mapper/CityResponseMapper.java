package com.heyrudy.mybatissample.controller.rest.dto.mapper;

import com.heyrudy.mybatissample.controller.rest.dto.CityResponseDTO;
import com.heyrudy.mybatissample.domain.model.city.FullCity;

public class CityResponseMapper {

    public static final CityResponseMapper CITY_RESPONSE_MAPPER = new CityResponseMapper();

    public FullCity toModel(CityResponseDTO dto) {
        return FullCity.builder()
            .name(dto.name())
            .state(dto.state())
            .country(dto.country())
            .build();
    }

    public CityResponseDTO toDto(FullCity model) {
        return CityResponseDTO.builder()
            .name(model.getName())
            .state(model.getState())
            .country(model.getCountry())
            .build();
    }
}
