package com.heyrudy.mybatissample.controller.rest;

import com.heyrudy.mybatissample.context.AppScopedDependencyLocator;
import com.heyrudy.mybatissample.controller.rest.dto.ApiErrorResponse;
import com.heyrudy.mybatissample.controller.rest.dto.CityRequestDTO;
import com.heyrudy.mybatissample.controller.rest.dto.mapper.CityRequestMapper;
import com.heyrudy.mybatissample.controller.rest.dto.mapper.CityResponseMapper;
import com.heyrudy.mybatissample.controller.rest.dto.validator.CityCriteriaValidator;
import com.heyrudy.mybatissample.controller.rest.dto.validator.CityRequestDTOValidator;
import com.heyrudy.mybatissample.domain.api.CreateCityAPI;
import com.heyrudy.mybatissample.domain.api.FindCitiesAPI;
import com.heyrudy.mybatissample.domain.api.FindCityByIdAPI;
import com.heyrudy.mybatissample.gateway.file.pdf.CreatePdfUtil;
import cyclops.control.Reader;
import io.vavr.control.Try;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

public enum CityCriticalRestAPIAdapter {
    INSTANCE;

    public static final Logger logger = LoggerFactory.getLogger(CityCriticalRestAPIAdapter.class);

    /**
     * @param request city with all its details to persist in the database
     * @return HTTP Response with persisted city in the database
     */
    public Reader<AppScopedDependencyLocator, ServerResponse> createCity(
        ServerRequest request) {
        return appScopedDependencyLocator ->
            Try.of(() -> request.body(CityRequestDTO.class))
                .toEither()
                .fold(
                    // Error handling for malformed JSON or validation failures
                    error ->
                        ServerResponse.badRequest()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(Map.of("error",
                                "Invalid request body: %s".formatted(error.getMessage()))),
                    cityRequestDTO ->
                        CityRequestDTOValidator.INSTANCE.validateCityRequestDTO(
                                cityRequestDTO.name(), cityRequestDTO.state(), cityRequestDTO.country())
                            .map(CityRequestMapper.INSTANCE::toModel)
                            .map(iCity ->
                                CreateCityAPI.INSTANCE.execute(iCity)
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
                                                ServerResponse.status(HttpStatus.FAILED_DEPENDENCY)
                                                    .body(
                                                        missingCityDbSPICriticalServiceError.getMessage()),
                                            iCity -> {
                                                logger.info("A new city is created");
                                                return ServerResponse.status(HttpStatus.CREATED)
                                                    .contentType(MediaType.APPLICATION_JSON)
                                                    .body(CityResponseMapper.INSTANCE.toDto(iCity));
                                            }
                                        )
                            )
                );
    }

    /**
     * @return HTTP Response with all cities fetched from the database
     */
    public Reader<AppScopedDependencyLocator, ServerResponse> findCities() {
        return appScopedDependencyLocator ->
            FindCitiesAPI.INSTANCE.execute()
                .apply(appScopedDependencyLocator)
                .fold(missingCityDbCriticalServiceError ->
                        ServerResponse.status(HttpStatus.FAILED_DEPENDENCY)
                            .body(
                                missingCityDbCriticalServiceError.getMessage()),
                    iCityList -> {
                        logger.info("All cities were found");
                        return ServerResponse.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(iCityList.stream()
                                .map(CityResponseMapper.INSTANCE::toDto)
                                .toList());
                    }
                );
    }

    /**
     * @param request city's id to fetch from the database
     * @return HTTP Response with required information about a city
     */
    public Reader<AppScopedDependencyLocator, ServerResponse> findCityById(
        ServerRequest request) {
        String id = request.pathVariable("id");
        return appScopedDependencyLocator ->
            CityCriteriaValidator.INSTANCE.validateCityCriteria(Long.parseLong(id))
                .map(it ->
                    FindCityByIdAPI.INSTANCE.execute(it)
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
                    cityNotFoundErrorICityEither ->
                        cityNotFoundErrorICityEither.fold(
                            cityNotFoundError -> {
                                logger.error(cityNotFoundError.getMessage());
                                return ServerResponse.badRequest()
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .body(
                                        ApiErrorResponse.builder()
                                            .reason(cityNotFoundError.getMessage())
                                            .build());
                            },
                            iCity -> {
                                logger.info("A city with id {} is found", id);
                                return ServerResponse.ok()
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .body(CityResponseMapper.INSTANCE.toDto(iCity));
                            }
                        )
                );
    }

    public Reader<AppScopedDependencyLocator, ServerResponse> downloadCityPdfReport() {
        return __ ->
            ServerResponse.ok()
                .headers(httpHeaders ->
                    httpHeaders.add("content-disposition",
                        "attachment; filename=%s".formatted("cityReport.pdf")))
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new ByteArrayResource(CreatePdfUtil.INSTANCE.createPdf()));
    }
}
