package com.heyrudy.mybatissample.entrypoint.rest;

import com.heyrudy.mybatissample.entrypoint.rest.dto.CityRequestDTO;
import com.heyrudy.mybatissample.entrypoint.rest.dto.CityResponseDTO;
import com.heyrudy.mybatissample.entrypoint.rest.mapper.CityRequestMapper;
import com.heyrudy.mybatissample.entrypoint.rest.mapper.CityResponseMapper;
import com.heyrudy.mybatissample.entrypoint.rest.dto.validator.CityCriteriaValidator;
import com.heyrudy.mybatissample.entrypoint.rest.dto.validator.CityRequestDTOValidator;
import com.heyrudy.mybatissample.core.service.CityService;
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

    CityRequestMapper requestMapper;
    CityResponseMapper responseMapper;
    CityService service;

    @PostMapping(value = "/cities")
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
