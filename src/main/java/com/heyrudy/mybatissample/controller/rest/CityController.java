package com.heyrudy.mybatissample.controller.rest;

import com.heyrudy.mybatissample.controller.rest.dto.CityRequestDTO;
import com.heyrudy.mybatissample.controller.rest.dto.CityResponseDTO;
import com.heyrudy.mybatissample.controller.rest.dto.mapper.CityRequestMapper;
import com.heyrudy.mybatissample.controller.rest.dto.mapper.CityResponseMapper;
import com.heyrudy.mybatissample.controller.rest.dto.validator.CityCriteriaValidator;
import com.heyrudy.mybatissample.domain.api.CityServiceAPI;
import io.vavr.collection.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static java.lang.String.format;

@RestController
@RequestMapping(value = "/api/v1")
@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CityController {

    public static final CityRequestDTOValidator CITY_REQUEST_DTO_VALIDATOR = CityRequestDTOValidator.CITY_REQUEST_DTO_VALIDATOR;
    public static final CityCriteriaValidator CITY_CRITERIA_VALIDATOR = CityCriteriaValidator.CITY_CRITERIA_VALIDATOR;
    public static final CityRequestMapper CITY_REQUEST_MAPPER = CityRequestMapper.CITY_RESQUEST_MAPPER;
    public static final CityResponseMapper CITY_RESPONSE_MAPPER = CityResponseMapper.CITY_RESPONSE_MAPPER;
    CityServiceAPI service;

    @PostMapping(value = "/cities")
    public ResponseEntity<Object> createCity(@RequestBody final CityRequestDTO dto) {
        return CITY_REQUEST_DTO_VALIDATOR.validateCityRequestDTO(dto.name(), dto.state(), dto.country())
                .map(CITY_REQUEST_MAPPER::toModel)
                .map(service::createCity)
                .map(CITY_RESPONSE_MAPPER::toDto)
                .fold(
                        validationErrorMessages -> {
                            String validationErrorMessageReduced =
                                    validationErrorMessages.toStream()
                                            .reduce((f, s) -> f.equals(s) ? f : s);
                            log.info(validationErrorMessageReduced);
                            return ResponseEntity.badRequest()
                                    .body(
                                            ApiErrorResponse.builder()
                                                    .reason(validationErrorMessageReduced)
                                                    .build()
                                    );
                        },
                        cityResponseDTO -> {
                            log.info("A new city is created");
                            return ResponseEntity.status(HttpStatus.CREATED)
                                    .body(cityResponseDTO);
                        }
                );
    }

    @GetMapping(value = "/cities")
    public ResponseEntity<java.util.List<CityResponseDTO>> findCities() {
        log.info("All cities were found");
        return ResponseEntity.ok()
                .body(
                        List.ofAll(service.findCities())
                                .map(CITY_RESPONSE_MAPPER::toDto)
                                .toJavaList()
                );
    }

    @GetMapping(value = "/cities/{cityId}")
    public ResponseEntity<Object> findCityById(@PathVariable(value = "cityId") long id) {
        return CITY_CRITERIA_VALIDATOR.validateCityCriteria(id)
                .map(service::findCityById)
                .fold(
                        validationErrorMessage -> {
                            log.error(validationErrorMessage);
                            return ResponseEntity.badRequest()
                                    .body(
                                            ApiErrorResponse.builder()
                                                    .reason(validationErrorMessage)
                                                    .build()
                                    );
                        },
                        cityNotFoundErrorCityEither ->
                                cityNotFoundErrorCityEither.fold(
                                        cityNotFoundError -> {
                                            log.error(cityNotFoundError.getMessage());
                                            return ResponseEntity.badRequest()
                                                    .body(
                                                            ApiErrorResponse.builder()
                                                                    .reason(cityNotFoundError.getMessage())
                                                                    .build()
                                                    );
                                        },
                                        city -> {
                                            log.info(format("A city with id %d is found", id));
                                            return ResponseEntity.ok()
                                                    .body(CITY_RESPONSE_MAPPER.toDto(city));
                                        }
                                )
                );
    }

    @Builder
    public record ApiErrorResponse(String reason) {
    }

}
