package com.heyrudy.mybatissample.controller.rest.dto.mapper;

import com.heyrudy.mybatissample.domain.model.city.FullCity;
import com.heyrudy.mybatissample.controller.rest.dto.CityRequestDTO;

public class CityRequestMapper {

    public static final CityRequestMapper CITY_REQUEST_MAPPER = new CityRequestMapper();

    public FullCity toModel(CityRequestDTO dto) {
        return FullCity.builder()
            .name(dto.name())
            .state(dto.state())
            .country(dto.country())
            .build();
    }

    public CityRequestDTO toDto(FullCity fullCity) {
        return CityRequestDTO.builder()
            .name(fullCity.getName())
            .state(fullCity.getState())
            .country(fullCity.getCountry())
            .build();
    }
}
