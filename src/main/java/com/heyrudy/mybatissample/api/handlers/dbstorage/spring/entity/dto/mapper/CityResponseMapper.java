package com.heyrudy.mybatissample.api.handlers.dbstorage.spring.entity.dto.mapper;

import com.heyrudy.mybatissample.api.handlers.dbstorage.spring.entity.dto.CityResponseDTO;
import com.heyrudy.mybatissample.api.handlers.dbstorage.spring.entity.CityEntity;
import com.heyrudy.mybatissample.api.utils.Mapper;
import org.springframework.stereotype.Component;

@Component
public class CityResponseMapper implements Mapper<CityEntity, CityResponseDTO> {

    public CityEntity toEntity(CityResponseDTO dto) {
        return CityEntity.builder()
                .name(dto.name())
                .state(dto.state())
                .country(dto.country())
                .build();
    }

    public CityResponseDTO toDto(CityEntity entity) {
        return CityResponseDTO.builder()
                .name(entity.getName())
                .state(entity.getState())
                .country(entity.getCountry())
                .build();
    }
}
