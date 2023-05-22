package com.heyrudy.mybatissample.gateway.store.spring.relational.entity.mapper;

import com.heyrudy.mybatissample.domain.model.city.City;
import com.heyrudy.mybatissample.gateway.store.spring.relational.entity.CityEntity;

public class CityEntityMapper {

    public static final CityEntityMapper CITY_ENTITY_MAPPER = new CityEntityMapper();

    public CityEntity toEntity(City city) {
        return CityEntity.builder()
                .name(city.getName())
                .state(city.getState())
                .country(city.getCountry())
                .build();
    }

    public City toModel(CityEntity city) {
        return City.builder()
                .name(city.getName())
                .state(city.getState())
                .country(city.getCountry())
                .build();
    }

}
