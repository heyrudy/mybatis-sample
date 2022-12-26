package com.heyrudy.mybatissample.dto.mapper;

import com.heyrudy.mybatissample.dto.CityDto;
import com.heyrudy.mybatissample.domain.City;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CityMapper {

    CityDto toCityDto(City city);

    @Mapping(target = "cityId", ignore = true)
    City toCity(CityDto dto);
}
