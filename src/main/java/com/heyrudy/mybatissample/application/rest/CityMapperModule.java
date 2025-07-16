package com.heyrudy.mybatissample.application.rest;

import com.heyrudy.mybatissample.application.rest.CityDTOModule.CityRequestDTO;
import com.heyrudy.mybatissample.application.rest.CityDTOModule.CityResponseDTO;
import com.heyrudy.mybatissample.domain.CityModelModule.FullCity;
import com.heyrudy.mybatissample.domain.CityModelModule.ICity;

public interface CityMapperModule {

    enum CityRequestMapper {
        INSTANCE;

        public ICity toModel(CityRequestDTO dto) {
            return FullCity.builder()
                .name(dto.name())
                .state(dto.state())
                .country(dto.country()).build();
        }
    }

    enum CityResponseMapper {
        INSTANCE;

        public CityResponseDTO toDto(ICity model) {
            return CityResponseDTO.builder()
                .name(model.getName())
                .state(model.getState())
                .country(model.getCountry()).build();
        }
    }
}
