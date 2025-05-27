package com.heyrudy.mybatissample.controller.rest.dto.mapper;

import com.heyrudy.mybatissample.controller.rest.dto.CityResponseDTO;
import com.heyrudy.mybatissample.domain.model.city.ICity;

public enum CityResponseMapper {
    INSTANCE;

    public CityResponseDTO toDto(ICity model) {
        return CityResponseDTO.builder()
            .name(model.getName())
            .state(model.getState())
            .country(model.getCountry()).build();
    }
}