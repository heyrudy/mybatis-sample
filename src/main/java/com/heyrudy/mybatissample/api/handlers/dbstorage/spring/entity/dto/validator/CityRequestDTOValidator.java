package com.heyrudy.mybatissample.api.handlers.dbstorage.spring.entity.dto.validator;

import com.heyrudy.mybatissample.api.handlers.dbstorage.spring.entity.dto.CityRequestDTO;
import io.vavr.collection.Seq;
import io.vavr.control.Validation;

public class CityRequestDTOValidator {

    public static Validation<Seq<String>, CityRequestDTO> validateCityRequestDTO(String name, String state, String country) {
        return Validation.combine(
                validateName(name),
                validateState(state),
                validateCountry(country)
        ).ap(CityRequestDTO::new);
    }

    private static Validation<String, String> validateName(String name) {
        return !name.isBlank()
                ? Validation.valid(name)
                : Validation.invalid("Données Invalides suivant le pattern défini");
    }

    private static Validation<String, String> validateState(String state) {
        return !state.isBlank()
                ? Validation.valid(state)
                : Validation.invalid("Données Invalides suivant le pattern défini");
    }

    private static Validation<String, String> validateCountry(String country) {
        return !country.isBlank()
                ? Validation.valid(country)
                : Validation.invalid("Données Invalides suivant le pattern défini");
    }
}
