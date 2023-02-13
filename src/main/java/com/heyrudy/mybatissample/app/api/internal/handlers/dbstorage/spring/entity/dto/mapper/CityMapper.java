package com.heyrudy.mybatissample.app.api.internal.handlers.dbstorage.spring.entity.dto.mapper;

import com.heyrudy.mybatissample.app.api.internal.handlers.dbstorage.spring.entity.dto.CityResponseDto;
import com.heyrudy.mybatissample.app.api.internal.handlers.dbstorage.spring.entity.City;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CityMapper {

    CityResponseDto toDto(City city);

    @Mapping(target = "id", ignore = true)
    City toEntity(CityResponseDto dto);
}
