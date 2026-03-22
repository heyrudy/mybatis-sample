package com.heyrudy.mybatissample.application;

import com.heyrudy.mybatissample.domain.UtilsModule;
import java.util.Objects;
import java.util.stream.Stream;

public interface CityDTOModule
    extends UtilsModule {

    record CityResponseDTO(String name, String state, String country) {

        public static CityResponseDTO empty() {
            return new CityResponseDTO("", "", "");
        }

        @SafeVarargs
        public static CityResponseDTO of(final MutatorStage<CityResponseDTO>... stages) {
            return Stream.of(stages)
                .filter(Objects::nonNull)
                .reduce(
                    CityResponseDTO.empty(),
                    (acc, stage) -> stage.apply(acc),
                    (_, right) -> right);
        }
    }

    record CityRequestDTO(String name, String state, String country) {

        public static CityRequestDTO empty() {
            return new CityRequestDTO("", "", "");
        }

        @SafeVarargs
        public static CityRequestDTO of(final MutatorStage<CityRequestDTO>... stages) {
            return Stream.of(stages)
                .filter(Objects::nonNull)
                .reduce(
                    CityRequestDTO.empty(),
                    (acc, stage) -> stage.apply(acc),
                    (_, right) -> right);
        }
    }

    enum CityResponseDTOMutatorStages {
        INSTANCE;

        public MutatorStage<CityResponseDTO> name(final String name) {
            return MutatorStage.of(
                name,
                (it, v) -> new CityResponseDTO(v, it.state, it.country)
            );
        }

        public MutatorStage<CityResponseDTO> state(final String state) {
            return MutatorStage.of(
                state,
                (it, v) -> new CityResponseDTO(it.name, v, it.country)
            );
        }

        public MutatorStage<CityResponseDTO> country(final String country) {
            return MutatorStage.of(
                country,
                (it, v) -> new CityResponseDTO(it.name, it.state, v)
            );
        }
    }

    enum CityRequestDTOMutatorStages {
        INSTANCE;

        public MutatorStage<CityRequestDTO> name(final String name) {
            return MutatorStage.of(
                name,
                (it, v) -> new CityRequestDTO(v, it.state, it.country)
            );
        }

        public MutatorStage<CityRequestDTO> state(final String state) {
            return MutatorStage.of(
                state,
                (it, v) -> new CityRequestDTO(it.name, v, it.country)
            );
        }

        public MutatorStage<CityRequestDTO> country(final String country) {
            return MutatorStage.of(
                country,
                (it, v) -> new CityRequestDTO(it.name, it.state, v)
            );
        }
    }
}