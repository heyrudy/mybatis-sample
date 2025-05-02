package com.heyrudy.mybatissample.controller.rest;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static io.vavr.Predicates.instanceOf;

import com.heyrudy.mybatissample.controller.rest.dto.ApiErrorResponse;
import com.heyrudy.mybatissample.controller.rest.dto.CityRequestDTO;
import com.heyrudy.mybatissample.controller.rest.dto.mapper.CityRequestMapper;
import com.heyrudy.mybatissample.controller.rest.dto.mapper.CityResponseMapper;
import com.heyrudy.mybatissample.controller.rest.dto.validator.CityCriteriaValidator;
import com.heyrudy.mybatissample.controller.rest.dto.validator.CityRequestDTOValidator;
import com.heyrudy.mybatissample.domain.api.CreateCityAPI;
import com.heyrudy.mybatissample.domain.api.FindCitiesAPI;
import com.heyrudy.mybatissample.domain.api.FindCityByIdAPI;
import com.heyrudy.mybatissample.domain.model.error.CityNotFoundError;
import com.heyrudy.mybatissample.domain.model.error.MissingCityDbCriticalServiceError;
import com.heyrudy.mybatissample.domain.model.utils.PartialWorkflow;
import com.heyrudy.mybatissample.domain.spi.config.AppScopedDependencyLocator;
import com.heyrudy.mybatissample.gateway.file.pdf.CreatePdfUtil;
import io.vavr.control.Try;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

public final class CityCriticalRestAPIAdapter {

    public static final Logger logger = LoggerFactory.getLogger(CityCriticalRestAPIAdapter.class);

    public static final CityRequestDTOValidator CITY_REQUEST_DTO_VALIDATOR =
        CityRequestDTOValidator.CITY_REQUEST_DTO_VALIDATOR;
    public static final CityCriteriaValidator CITY_CRITERIA_VALIDATOR =
        CityCriteriaValidator.CITY_CRITERIA_VALIDATOR;

    public static final CityRequestMapper CITY_REQUEST_MAPPER = CityRequestMapper.INSTANCE;
    public static final CityResponseMapper CITY_RESPONSE_MAPPER = CityResponseMapper.INSTANCE;

    public static final CreateCityAPI CREATE_CITY_API = CreateCityAPI.INSTANCE;
    public static final FindCitiesAPI FIND_CITIES_API = FindCitiesAPI.INSTANCE;
    public static final FindCityByIdAPI FIND_CITY_BY_ID_API = FindCityByIdAPI.INSTANCE;
    public static final CreatePdfUtil CREATE_PDF_UTIL = CreatePdfUtil.INSTANCE;

    public static final CityCriticalRestAPIAdapter INSTANCE = new CityCriticalRestAPIAdapter();

    private CityCriticalRestAPIAdapter() {
        super();
    }

    /**
     * @param request city with all its details to persist in the database
     * @return HTTP Response with persisted city in the database
     */
    public PartialWorkflow<AppScopedDependencyLocator, ServerResponse> createCity(
        ServerRequest request) {
        return appScopedDependencyLocator ->
            Try.of(() -> request.body(CityRequestDTO.class))
                .toEither()
                .fold(
                    error -> {
                        // Error handling for malformed JSON or validation failures
                        return ServerResponse.badRequest()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(Map.of("error", "Invalid request body: " + error.getMessage()));
                    },
                    cityRequestDTO ->
                        CITY_REQUEST_DTO_VALIDATOR.validateCityRequestDTO(
                                cityRequestDTO.name(), cityRequestDTO.state(), cityRequestDTO.country())
                            .map(CITY_REQUEST_MAPPER::toModel)
                            .map(it ->
                                CREATE_CITY_API.execute(it)
                                    .apply(appScopedDependencyLocator))
                            .fold(
                                validationErrorMessages -> {
                                    String validationErrorMessageReduced =
                                        validationErrorMessages.toStream()
                                            .reduce((f, s) -> f.equals(s) ? f : s);
                                    logger.info(validationErrorMessageReduced);
                                    return ServerResponse.badRequest()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(
                                            ApiErrorResponse.builder()
                                                .reason(validationErrorMessageReduced)
                                                .build());
                                },
                                missingCityDbCriticalServiceErrorICityEither ->
                                    missingCityDbCriticalServiceErrorICityEither
                                        .fold(
                                            missingCityDbSPICriticalServiceError ->
                                                ServerResponse.status(
                                                        HttpStatus.FAILED_DEPENDENCY)
                                                    .body(
                                                        missingCityDbSPICriticalServiceError.getMessage()),
                                            iCity -> {
                                                logger.info("A new city is created");
                                                return ServerResponse.status(HttpStatus.CREATED)
                                                    .contentType(MediaType.APPLICATION_JSON)
                                                    .body(CITY_RESPONSE_MAPPER.toDto(iCity));
                                            }
                                        )
                            )
                );
    }

    /**
     * @return HTTP Response with all cities fetched from the database
     */
    public PartialWorkflow<AppScopedDependencyLocator, ServerResponse> findCities() {
        logger.info("All cities were found");
        return appScopedDependencyLocator ->
            FIND_CITIES_API.execute()
                .apply(appScopedDependencyLocator)
                .fold(missingCityDbCriticalServiceError ->
                        ServerResponse.status(HttpStatus.FAILED_DEPENDENCY)
                            .body(
                                missingCityDbCriticalServiceError.getMessage()),
                    iCityList -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(iCityList.stream()
                            .map(CITY_RESPONSE_MAPPER::toDto)
                            .toList())
                );
    }

    /**
     * @param request city's id to fetch from the database
     * @return HTTP Response with required information about a city
     */
    public PartialWorkflow<AppScopedDependencyLocator, ServerResponse> findCityById(
        ServerRequest request) {
        String id = request.pathVariable("id");
        return appScopedDependencyLocator ->
            CITY_CRITERIA_VALIDATOR.validateCityCriteria(Long.parseLong(id))
                .map(it ->
                    FIND_CITY_BY_ID_API.execute(it)
                        .apply(appScopedDependencyLocator))
                .fold(
                    validationErrorMessage -> {
                        logger.error(validationErrorMessage);
                        return ServerResponse.badRequest()
                            .body(
                                ApiErrorResponse.builder()
                                    .reason(validationErrorMessage)
                                    .build());
                    },
                    missingCityErrorICityEither ->
                        missingCityErrorICityEither.fold(
                            missingCityError ->
                                Match(missingCityError).of(
                                    Case($(instanceOf(
                                            MissingCityDbCriticalServiceError.class)),
                                        it -> {
                                            logger.error(it.getMessage());
                                            return ServerResponse.status(
                                                    HttpStatus.FAILED_DEPENDENCY)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .body(
                                                    ApiErrorResponse.builder()
                                                        .reason(it.getMessage())
                                                        .build());
                                        }),
                                    Case($(instanceOf(CityNotFoundError.class)),
                                        it -> {
                                            logger.error(it.getMessage());
                                            return ServerResponse.badRequest()
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .body(
                                                    ApiErrorResponse.builder()
                                                        .reason(it.getMessage())
                                                        .build());
                                        })),
                            iCity -> {
                                logger.info("A city with id {} is found", id);
                                return ServerResponse.ok()
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .body(CITY_RESPONSE_MAPPER.toDto(iCity));
                            }
                        )
                );
    }

    public PartialWorkflow<AppScopedDependencyLocator, ServerResponse> downloadCityPdfReport() {
        return appScopedSecretLocator ->
            ServerResponse.ok()
                .headers(httpHeaders ->
                    httpHeaders.add("content-disposition",
                        "attachment; filename=%s".formatted("cityReport.pdf")))
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new ByteArrayResource(CREATE_PDF_UTIL.createPdf()));
    }
}
