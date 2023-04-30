package com.heyrudy.mybatissample.api.handlers.dbstorage.spring.entity.dto.mapper;

import com.heyrudy.mybatissample.api.handlers.dbstorage.spring.entity.CityEntity;
import com.heyrudy.mybatissample.api.handlers.dbstorage.spring.entity.dto.CityRequestDTO;
import com.heyrudy.mybatissample.api.utils.Mapper;
import org.springframework.stereotype.Component;

@Component
public class CityRequestMapper implements Mapper<CityEntity, CityRequestDTO> {
    public CityEntity toEntity(CityRequestDTO dto) {
        return CityEntity.builder()
                .name(dto.name())
                .state(dto.state())
                .country(dto.country())
                .build();
    }

    public CityRequestDTO toDto(CityEntity entity) {
        return CityRequestDTO.builder()
                .name(entity.getName())
                .state(entity.getState())
                .country(entity.getCountry())
                .build();
    }
}
