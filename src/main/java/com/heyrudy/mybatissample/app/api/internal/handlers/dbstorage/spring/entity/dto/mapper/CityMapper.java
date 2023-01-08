package com.heyrudy.mybatissample.app.api.internal.handlers.dbstorage.spring.entity.dto.mapper;

import com.heyrudy.mybatissample.app.api.internal.handlers.dbstorage.spring.entity.dto.CityDto;
import com.heyrudy.mybatissample.app.api.internal.handlers.dbstorage.spring.entity.City;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CityMapper {

    CityDto toCityDto(City city);

    @Mapping(target = "id", ignore = true)
    City toCity(CityDto dto);
}
