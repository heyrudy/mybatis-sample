package com.heyrudy.mybatissample.controller.rest.dto.validator;

import com.heyrudy.mybatissample.controller.rest.dto.CityRequestDTO;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;

public class CityRequestDTOValidator {

    public static final String INVALID_INPUT_MESSAGE = "Données Invalides suivant le pattern défini";

    private CityRequestDTOValidator() {
        throw new IllegalStateException("Validation utility class");
    }

    public static Validation<Seq<String>, CityRequestDTO> validateCityRequestDTO(String name, String state, String country) {
        return Validation.combine(
                validateName(name),
                validateState(state),
                validateCountry(country)
        ).ap(CityRequestDTO::new);
    }

    private static Validation<String, String> validateName(String name) {
        return name.isBlank()
                ? Validation.invalid(INVALID_INPUT_MESSAGE)
                : Validation.valid(name);
    }

    private static Validation<String, String> validateState(String state) {
        return state.isBlank()
                ? Validation.invalid(INVALID_INPUT_MESSAGE)
                : Validation.valid(state);
    }

    private static Validation<String, String> validateCountry(String country) {
        return country.isBlank()
                ? Validation.invalid(INVALID_INPUT_MESSAGE)
                : Validation.valid(country);
    }
}
