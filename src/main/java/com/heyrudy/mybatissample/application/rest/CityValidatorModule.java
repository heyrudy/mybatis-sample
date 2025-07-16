package com.heyrudy.mybatissample.application.rest;

import com.heyrudy.mybatissample.application.rest.CityDTOModule.CityRequestDTO;
import com.heyrudy.mybatissample.domain.CityModelModule.CityCriteriaDetails;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;

public interface CityValidatorModule {

    enum CityRequestDTOValidator {
        INSTANCE;

        public Validation<Seq<String>, CityRequestDTO> validateCityRequestDTO(
            String name, String state, String country) {
            return Validation.combine(
                validateName(name),
                validateState(state),
                validateCountry(country)
            ).ap(CityRequestDTO::new);
        }

        private Validation<String, String> validateName(String name) {
            return name.isBlank()
                ? Validation.invalid(InvalidationErrorMessages.INVALID_INPUT_MESSAGE)
                : Validation.valid(name);
        }

        private Validation<String, String> validateState(String state) {
            return state.isBlank()
                ? Validation.invalid(InvalidationErrorMessages.INVALID_INPUT_MESSAGE)
                : Validation.valid(state);
        }

        private Validation<String, String> validateCountry(String country) {
            return country.isBlank()
                ? Validation.invalid(InvalidationErrorMessages.INVALID_INPUT_MESSAGE)
                : Validation.valid(country);
        }

        public static class InvalidationErrorMessages {

            public static final String INVALID_INPUT_MESSAGE =
                "Données Invalides suivant le pattern défini";
        }

    }

    enum CityCriteriaValidator {
        INSTANCE;

        public Validation<String, CityCriteriaDetails> validateCityCriteria(long cityId) {
            return validateCityId(cityId).map(CityCriteriaDetails::new);
        }

        private Validation<String, Long> validateCityId(long cityId) {
            return cityId <= InvalidState.INCORRECT_CITY_ID
                ? Validation.invalid(InvalidationErrorMessages.INVALID_INPUT_MESSAGE)
                : Validation.valid(cityId);
        }

        public static class InvalidState {

            private static final int INCORRECT_CITY_ID = 0;
        }

        public static class InvalidationErrorMessages {

            public static final String INVALID_INPUT_MESSAGE =
                "ID must be greater than %d".formatted(InvalidState.INCORRECT_CITY_ID);
        }
    }
}
