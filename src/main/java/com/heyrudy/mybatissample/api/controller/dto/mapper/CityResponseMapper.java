package com.heyrudy.mybatissample.api.controller.dto.mapper;

import com.heyrudy.mybatissample.api.controller.dto.CityResponseDTO;
import com.heyrudy.mybatissample.api.controller.utils.Mapper;
import com.heyrudy.mybatissample.core.model.City;
import org.springframework.stereotype.Component;

@Component
public class CityResponseMapper implements Mapper<City, CityResponseDTO> {

    public City toEntity(CityResponseDTO dto) {
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