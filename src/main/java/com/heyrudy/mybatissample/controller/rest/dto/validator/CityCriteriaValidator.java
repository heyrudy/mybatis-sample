package com.heyrudy.mybatissample.controller.rest.dto.validator;

import com.heyrudy.mybatissample.domain.model.common.CityCriteriaDetails;
import io.vavr.control.Validation;

public enum CityCriteriaValidator {
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