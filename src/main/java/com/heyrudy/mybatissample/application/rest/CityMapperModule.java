package com.heyrudy.mybatissample.application.rest;

import com.heyrudy.mybatissample.domain.CityModelModule;

public interface CityMapperModule
    extends CityModelModule,
    CityDTOModule {

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
