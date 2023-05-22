package com.heyrudy.mybatissample.controller.rest.dto.validator;

import com.heyrudy.mybatissample.controller.rest.dto.CityCriteriaDTO;
import io.vavr.control.Validation;

public class CityCriteriaValidator {

    private static final int INCORRECT_CITY_ID = 0;

    private CityCriteriaValidator() {
        throw new IllegalStateException("Validation utility class");
    }

    public static Validation<String, CityCriteriaDTO> validateCityCriteria(long cityId) {
        return validateCityId(cityId).map(CityCriteriaDTO::new);
    }

    private static Validation<String, Long> validateCityId(long cityId) {
        return cityId <= INCORRECT_CITY_ID
                ? Validation.invalid("ID must be greater than " + INCORRECT_CITY_ID)
                : Validation.valid(cityId);
    }
}
