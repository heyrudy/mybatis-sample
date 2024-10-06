package com.heyrudy.mybatissample.gateway.db.spring.relational.entity.mapper;

import com.heyrudy.mybatissample.domain.model.city.FullCity;
import com.heyrudy.mybatissample.gateway.db.spring.relational.entity.CityEntity;

public class CityEntityMapper {

    public static final CityEntityMapper CITY_ENTITY_MAPPER = new CityEntityMapper();

    public CityEntity toEntity(FullCity fullCity) {
        return CityEntity.builder()
            .name(fullCity.getName())
            .state(fullCity.getState())
            .country(fullCity.getCountry())
            .build();
    }

    public FullCity toModel(CityEntity city) {
        return FullCity.builder()
            .name(city.getName())
            .state(city.getState())
            .country(city.getCountry())
            .build();
    }
}
