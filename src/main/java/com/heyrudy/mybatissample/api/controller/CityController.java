package com.heyrudy.mybatissample.api.controller;

import com.heyrudy.mybatissample.api.handlers.dbstorage.spring.entity.dto.CityCriteriaDTO;
import com.heyrudy.mybatissample.api.handlers.dbstorage.spring.entity.dto.CityRequestDTO;
import com.heyrudy.mybatissample.api.handlers.dbstorage.spring.entity.dto.CityResponseDTO;
import com.heyrudy.mybatissample.api.handlers.dbstorage.spring.entity.dto.mapper.CityRequestMapper;
import com.heyrudy.mybatissample.api.handlers.dbstorage.spring.entity.dto.mapper.CityResponseMapper;
import com.heyrudy.mybatissample.api.service.CityService;
import io.vavr.collection.List;
import io.vavr.control.Option;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.heyrudy.mybatissample.api.handlers.dbstorage.spring.entity.dto.validator.CityCriteriaValidator.validateCityCriteria;
import static com.heyrudy.mybatissample.api.handlers.dbstorage.spring.entity.dto.validator.CityRequestDTOValidator.validateCityRequestDTO;
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
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<?> createCity(@RequestBody final CityRequestDTO dto) {
        return validateCityRequestDTO(dto.name(), dto.state(), dto.country())
                .map(requestMapper::toEntity)
                .map(service::createCity)
                .map(responseMapper::toDto)
                .fold(
                        error -> ResponseEntity.badRequest().body(error.toStream().reduce((f, s) -> f.equals(s) ? f : s)),
                        cityResponseDTO -> {
                            log.info("A new city is created");
                            return ResponseEntity.ok().body(cityResponseDTO);
                        }
                );
    }

    @GetMapping(value = "/cities")
    public ResponseEntity<List<CityResponseDTO>> findCities() {
        log.info("All cities were found");
        return ResponseEntity.ok()
                .body(
                        List.ofAll(service.findCities()).map(responseMapper::toDto)
                );
    }

    @GetMapping(value = "/cities")
    public ResponseEntity<?> findCityById(final CityCriteriaDTO cityCriteriaDTO) {
        return validateCityCriteria(cityCriteriaDTO.cityId())
                .map(service::findCityById)
                .map(Option::get)
                .map(responseMapper::toDto)
                .fold(
                        error -> ResponseEntity.badRequest().body(error),
                        cityResponseDto -> {
                            log.info(format("A city with id %d is found", cityCriteriaDTO.cityId()));
                            return ResponseEntity.ok().body(cityResponseDto);
                        }
                );
    }

}
