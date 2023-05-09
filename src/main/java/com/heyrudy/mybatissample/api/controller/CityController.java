package com.heyrudy.mybatissample.api.controller;

import com.heyrudy.mybatissample.api.controller.dto.CityRequestDTO;
import com.heyrudy.mybatissample.api.controller.dto.CityResponseDTO;
import com.heyrudy.mybatissample.api.controller.dto.mapper.CityRequestMapper;
import com.heyrudy.mybatissample.api.controller.dto.mapper.CityResponseMapper;
import com.heyrudy.mybatissample.api.controller.dto.validator.CityCriteriaValidator;
import com.heyrudy.mybatissample.api.controller.dto.validator.CityRequestDTOValidator;
import com.heyrudy.mybatissample.api.service.CityServiceAPI;
import io.vavr.collection.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static java.lang.String.format;

@RestController
@RequestMapping(value = "/api/v1")
@Slf4j
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CityController {

    CityRequestMapper requestMapper;
    CityResponseMapper responseMapper;
    CityServiceAPI service;

    @PostMapping(value = "/cities")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<Object> createCity(@RequestBody final CityRequestDTO dto) {
        return CityRequestDTOValidator.validateCityRequestDTO(dto.name(), dto.state(), dto.country())
                .map(requestMapper::toEntity)
                .map(service::createCity)
                .map(responseMapper::toDto)
                .fold(
                        validationErrorMessages -> {
                            String validationErrorMessageReduced =
                                    validationErrorMessages.toStream()
                                            .reduce((f, s) -> f.equals(s) ? f : s);
                            log.info(validationErrorMessageReduced);
                            return ResponseEntity.badRequest()
                                    .body(
                                            validationErrorMessageReduced
                                    );
                        },
                        cityResponseDTO -> {
                            log.info("A new city is created");
                            return ResponseEntity.ok()
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
                                .map(responseMapper::toDto)
                                .toJavaList()
                );
    }

    @GetMapping(value = "/cities/{cityId}")
    public ResponseEntity<Object> findCityById(@PathVariable(value = "cityId") long id) {
        return CityCriteriaValidator.validateCityCriteria(id)
                .map(service::findCityById)
                .fold(
                        validationErrorMessage -> {
                            log.error(validationErrorMessage);
                            return ResponseEntity.badRequest()
                                    .body(
                                            ErrorResponse.builder()
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
                                                            ErrorResponse.builder()
                                                                    .reason(cityNotFoundError.getMessage())
                                                                    .build()
                                                    );
                                        },
                                        city -> {
                                            log.info(format("A city with id %d is found", id));
                                            return ResponseEntity.ok()
                                                    .body(responseMapper.toDto(city));
                                        }
                                )
                );
    }

    @Builder
    public record ErrorResponse(String reason) {
    }

}
