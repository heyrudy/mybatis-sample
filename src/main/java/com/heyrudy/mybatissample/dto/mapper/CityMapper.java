package com.heyrudy.mybatissample.dto.mapper;

import com.heyrudy.mybatissample.dto.CityDto;
import com.heyrudy.mybatissample.domain.City;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CityMapper {

    CityDto cityToCityDto(City city);

    City cityDtoToCity(CityDto dto);
}
