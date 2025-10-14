package com.heyrudy.mybatissample.application;

import com.heyrudy.mybatissample.domain.UtilsModule;
import java.util.Arrays;
import java.util.Objects;

public interface CityDTOModule
    extends UtilsModule {

    record CityResponseDTO(String name, String state, String country) {

        public CityResponseDTO() {
            this("", "", "");
        }

        @SafeVarargs
        public static CityResponseDTO of(MutatorOption<CityResponseDTO>... options) {
            return Arrays.stream(options)
                .filter(Objects::nonNull)
                .reduce(new CityResponseDTO(), (dto, option) -> option.apply(dto),
                    (a, b) -> a);
        }
    }

    record CityRequestDTO(String name, String state, String country) {

        public CityRequestDTO() {
            this("", "", "");
        }

        @SafeVarargs
        public static CityRequestDTO of(MutatorOption<CityRequestDTO>... options) {
            return Arrays.stream(options)
                .filter(Objects::nonNull)
                .reduce(new CityRequestDTO(), (dto, option) -> option.apply(dto),
                    (a, b) -> a);
        }
    }

    enum CityResponseDTOMutatorOptions {
        INSTANCE;

        public MutatorOption<CityResponseDTO> name(String name) {
            return MutatorOption.of(
                name,
                (it, v) -> new CityResponseDTO(v, it.state, it.country)
            );
        }

        public MutatorOption<CityResponseDTO> state(String state) {
            return MutatorOption.of(
                state,
                (it, v) -> new CityResponseDTO(it.name, v, it.country)
            );
        }

        public MutatorOption<CityResponseDTO> country(String country) {
            return MutatorOption.of(
                country,
                (it, v) -> new CityResponseDTO(it.name, it.state, v)
            );
        }
    }

    enum CityRequestDTOMutatorOptions {
        INSTANCE;

        public MutatorOption<CityRequestDTO> name(String name) {
            return MutatorOption.of(
                name,
                (it, v) -> new CityRequestDTO(v, it.state, it.country)
            );
        }

        public MutatorOption<CityRequestDTO> state(String state) {
            return MutatorOption.of(
                state,
                (it, v) -> new CityRequestDTO(it.name, v, it.country)
            );
        }

        public MutatorOption<CityRequestDTO> country(String country) {
            return MutatorOption.of(
                country,
                (it, v) -> new CityRequestDTO(it.name, it.state, v)
            );
        }
    }
}
