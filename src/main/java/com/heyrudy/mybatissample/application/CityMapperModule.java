package com.heyrudy.mybatissample.application;

import com.heyrudy.mybatissample.domain.CityModelModule;

public interface CityMapperModule
    extends CityModelModule,
    CityDTOModule {

    enum CityRequestMapper {
        INSTANCE;

        public ICity toModel(CityRequestDTO dto) {
            return FullCity.of(
                FullCityMutatorStages.INSTANCE.name(dto.name()),
                FullCityMutatorStages.INSTANCE.state(dto.state()),
                FullCityMutatorStages.INSTANCE.country(dto.country()));
        }
    }

    enum CityResponseMapper {
        INSTANCE;

        public CityResponseDTO toDto(ICity model) {
            return CityResponseDTO.of(
                CityResponseDTOMutatorStages.INSTANCE.name(model.getName()),
                CityResponseDTOMutatorStages.INSTANCE.state(model.getState()),
                CityResponseDTOMutatorStages.INSTANCE.country(model.getCountry()));
        }
    }
}