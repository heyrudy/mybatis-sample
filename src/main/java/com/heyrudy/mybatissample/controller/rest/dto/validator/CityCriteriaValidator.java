package com.heyrudy.mybatissample.controller.rest.dto.validator;

import com.heyrudy.mybatissample.domain.model.common.CityCriteriaDTO;
import io.vavr.control.Validation;

public class CityCriteriaValidator {

    public static final CityCriteriaValidator CITY_CRITERIA_VALIDATOR = new CityCriteriaValidator();

    public Validation<String, CityCriteriaDTO> validateCityCriteria(long cityId) {
        return validateCityId(cityId).map(CityCriteriaDTO::new);
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
            "ID must be greater than " + InvalidState.INCORRECT_CITY_ID;
    }
}
