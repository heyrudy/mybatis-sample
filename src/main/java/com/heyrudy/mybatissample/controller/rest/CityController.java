package com.heyrudy.mybatissample.controller.rest;

import static java.lang.String.format;

import com.heyrudy.mybatissample.controller.rest.dto.ApiErrorResponse;
import com.heyrudy.mybatissample.controller.rest.dto.CityRequestDTO;
import com.heyrudy.mybatissample.controller.rest.dto.CityResponseDTO;
import com.heyrudy.mybatissample.controller.rest.dto.mapper.CityRequestMapper;
import com.heyrudy.mybatissample.controller.rest.dto.mapper.CityResponseMapper;
import com.heyrudy.mybatissample.controller.rest.dto.validator.CityCriteriaValidator;
import com.heyrudy.mybatissample.controller.rest.dto.validator.CityRequestDTOValidator;
import com.heyrudy.mybatissample.domain.api.CreateCityInteractorAPI;
import com.heyrudy.mybatissample.domain.api.FindCitiesInteractorAPI;
import com.heyrudy.mybatissample.domain.api.FindCityByIdInteractorAPI;
import com.heyrudy.mybatissample.gateway.utils.CreatePdfUtil;
import io.vavr.collection.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1")
public class CityController {

    public static final Logger logger = LoggerFactory.getLogger(CityController.class);
    public static final CityRequestDTOValidator CITY_REQUEST_DTO_VALIDATOR = CityRequestDTOValidator.CITY_REQUEST_DTO_VALIDATOR;
    public static final CityCriteriaValidator CITY_CRITERIA_VALIDATOR = CityCriteriaValidator.CITY_CRITERIA_VALIDATOR;
    public static final CityRequestMapper CITY_REQUEST_MAPPER = CityRequestMapper.CITY_REQUEST_MAPPER;
    public static final CityResponseMapper CITY_RESPONSE_MAPPER = CityResponseMapper.CITY_RESPONSE_MAPPER;
    public static final CreatePdfUtil CREATE_PDF_UTIL = CreatePdfUtil.CREATE_PDF_UTIL;
    private final CreateCityInteractorAPI createCityInteractorAPI;
    private final FindCityByIdInteractorAPI findCityByIdInteractorAPI;
    private final FindCitiesInteractorAPI findCitiesInteractorAPI;

    public CityController(
        CreateCityInteractorAPI createCityInteractorAPI,
        FindCityByIdInteractorAPI findCityByIdInteractorAPI,
        FindCitiesInteractorAPI findCitiesInteractorAPI) {
        this.createCityInteractorAPI = createCityInteractorAPI;
        this.findCityByIdInteractorAPI = findCityByIdInteractorAPI;
        this.findCitiesInteractorAPI = findCitiesInteractorAPI;
    }

    @PostMapping(value = "/cities")
    public ResponseEntity<Object> createCity(@RequestBody final CityRequestDTO dto) {
        return CITY_REQUEST_DTO_VALIDATOR.validateCityRequestDTO(dto.name(), dto.state(),
                dto.country())
            .map(CITY_REQUEST_MAPPER::toModel)
            .map(createCityInteractorAPI::createCity)
            .map(CITY_RESPONSE_MAPPER::toDto)
            .fold(
                validationErrorMessages -> {
                    String validationErrorMessageReduced =
                        validationErrorMessages.toStream()
                            .reduce((f, s) -> f.equals(s) ? f : s);
                    logger.info(validationErrorMessageReduced);
                    return ResponseEntity.badRequest()
                        .body(
                            ApiErrorResponse.builder()
                                .reason(validationErrorMessageReduced)
                                .build()
                        );
                },
                cityResponseDTO -> {
                    logger.info("A new city is created");
                    return ResponseEntity.status(HttpStatus.CREATED)
                        .body(cityResponseDTO);
                }
            );
    }

    @GetMapping(value = "/cities")
    public ResponseEntity<java.util.List<CityResponseDTO>> findCities() {
        logger.info("All cities were found");
        return ResponseEntity.ok()
            .body(
                List.ofAll(findCitiesInteractorAPI.findCities())
                    .map(CITY_RESPONSE_MAPPER::toDto)
                    .toJavaList()
            );
    }

    @GetMapping(value = "/cities/{cityId}")
    public ResponseEntity<Object> findCityById(@PathVariable(value = "cityId") long id) {
        return CITY_CRITERIA_VALIDATOR.validateCityCriteria(id)
            .map(findCityByIdInteractorAPI::findCityById)
            .fold(
                validationErrorMessage -> {
                    logger.error(validationErrorMessage);
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
                            logger.error(cityNotFoundError.getMessage());
                            return ResponseEntity.badRequest()
                                .body(
                                    ApiErrorResponse.builder()
                                        .reason(cityNotFoundError.getMessage())
                                        .build()
                                );
                        },
                        city -> {
                            logger.info(format("A city with id %d is found", id));
                            return ResponseEntity.ok()
                                .body(CITY_RESPONSE_MAPPER.toDto(city));
                        }
                    )
            );
    }

    @GetMapping(value = "/cities/download")
    public ResponseEntity<Resource> downloadCityPdfReport() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(
            "content-disposition",
            "attachment; filename=%s".formatted("cityReport.pdf")
        );

        return ResponseEntity.ok()
            .headers(headers)
            .body(
                new ByteArrayResource(CREATE_PDF_UTIL.createPdf())
            );
    }

}
