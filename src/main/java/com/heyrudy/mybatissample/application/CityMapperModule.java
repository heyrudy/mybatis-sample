package com.heyrudy.mybatissample.application;

import com.heyrudy.mybatissample.domain.CityModelModule;

public interface CityMapperModule
    extends CityModelModule,
    CityDTOModule {

    enum CityRequestMapper {
        INSTANCE;

        public ICity toModel(CityRequestDTO dto) {
            return FullCity.of(
                FullCityMutatorOptions.INSTANCE.name(dto.name()),
                FullCityMutatorOptions.INSTANCE.state(dto.state()),
                FullCityMutatorOptions.INSTANCE.country(dto.country()));
        }
    }

    enum CityResponseMapper {
        INSTANCE;

        public CityResponseDTO toDto(ICity model) {
            return CityResponseDTO.of(
                CityResponseDTOMutatorOptions.INSTANCE.name(model.getName()),
                CityResponseDTOMutatorOptions.INSTANCE.state(model.getState()),
                CityResponseDTOMutatorOptions.INSTANCE.country(model.getCountry()));
        }
    }
}
