package com.heyrudy.mybatissample.controller.rest.dto.mapper;

import com.heyrudy.mybatissample.controller.rest.dto.CityRequestDTO;
import com.heyrudy.mybatissample.domain.model.city.FullCity;
import com.heyrudy.mybatissample.domain.model.city.ICity;

public class CityRequestMapper {

    public static final CityRequestMapper INSTANCE = new CityRequestMapper();

    private CityRequestMapper() {
        super();
    }

    public ICity toModel(CityRequestDTO dto) {
        return FullCity.builder()
            .name(dto.name())
            .state(dto.state())
            .country(dto.country()).build();
    }
}
