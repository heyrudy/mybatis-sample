package com.heyrudy.mybatissample.gateway.db.spring.relational.entity.mapper;

import com.heyrudy.mybatissample.domain.model.city.FullCity;
import com.heyrudy.mybatissample.domain.model.city.ICity;
import com.heyrudy.mybatissample.gateway.db.spring.relational.entity.CityEntity;

public class CityEntityMapper {

    public static final CityEntityMapper CITY_ENTITY_MAPPER = new CityEntityMapper();

    public CityEntity toEntity(ICity iCity) {
        return CityEntity.builder()
            .id(iCity.getId())
            .name(iCity.getName())
            .state(iCity.getState())
            .country(iCity.getCountry()).build();
    }

    public ICity toModel(CityEntity city) {
        return FullCity.builder()
            .id(city.getId())
            .name(city.getName())
            .state(city.getState())
            .country(city.getCountry()).build();
    }
}
