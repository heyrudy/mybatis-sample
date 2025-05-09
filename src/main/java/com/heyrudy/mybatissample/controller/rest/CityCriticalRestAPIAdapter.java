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
import com.heyrudy.mybatissample.domain.model.city.ICity;
import com.heyrudy.mybatissample.domain.model.error.CriticalRepositoryNotFoundByDependencyLocatorError;
import com.heyrudy.mybatissample.gateway.file.pdf.CreatePdfUtil;
import cyclops.control.Reader;
import io.vavr.collection.Seq;
import io.vavr.control.Either;
import io.vavr.control.Try;
import io.vavr.control.Validation;
import java.util.function.Function;
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
        // 1) Define function to parse JSON body into DTO
        Reader<AppScopedDependencyLocator, Either<String, CityRequestDTO>> parseBodyReader =
            __ ->
                Try.of(() -> request.body(CityRequestDTO.class))
                    .toEither()
                    .mapLeft(Throwable::getMessage);
        // 2) Define a function to validate a DTO,
        // returning Either with validation errors or valid DTO
        Function<CityRequestDTO, Either<String, CityRequestDTO>> validateDto =
            cityRequestDTO -> {
                Validation<Seq<String>, CityRequestDTO> cityRequestDTOValidation =
                    CityRequestDTOValidator.INSTANCE.validateCityRequestDTO(
                        cityRequestDTO.name(), cityRequestDTO.state(), cityRequestDTO.country());

                return cityRequestDTOValidation.isValid()
                    ? Either.right(cityRequestDTO)
                    : Either.left(
                        String.valueOf(cityRequestDTOValidation.toStream().reduce((a, b) -> a.equals(b) ? a : b)));
            };
        // 3) Define a function that creates a Reader for handling validation error
        Function<String, Reader<AppScopedDependencyLocator, Either<CriticalRepositoryNotFoundByDependencyLocatorError, ICity>>>
            constantErrorReader =
            errMsg ->
                __ -> Either.left(
                    new CriticalRepositoryNotFoundByDependencyLocatorError(errMsg));
        // 4) Define function to transform DTO to model and execute create operation
        Function<CityRequestDTO, Reader<AppScopedDependencyLocator, Either<CriticalRepositoryNotFoundByDependencyLocatorError, ICity>>>
            dtoToCreateCity =
            cityRequestDTO ->
                CreateCityAPI.INSTANCE.execute(CityRequestMapper.INSTANCE.toModel(cityRequestDTO));
        // 5) Define function to create error response
        Function<CriticalRepositoryNotFoundByDependencyLocatorError, ServerResponse> createErrorResponse =
            criticalRepositoryNotFoundByDependencyLocatorError ->
                ServerResponse.status(HttpStatus.FAILED_DEPENDENCY)
                    .body(criticalRepositoryNotFoundByDependencyLocatorError.getMessage());
        // 6) Define function to create a success response
        Function<ICity, ServerResponse> createSuccessResponse =
            iCity ->
                ServerResponse.status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(CityResponseMapper.INSTANCE.toDto(iCity));
        // Compose operations with flatMap to explicitly avoid apply
        return parseBodyReader
            .map(parsedBodyEither -> parsedBodyEither.flatMap(validateDto))
            .flatMap(validatedDtoEither ->
                validatedDtoEither.fold(constantErrorReader, dtoToCreateCity))
            .map(criticalRepositoryNotFoundByDependencyLocatorErrorICityEither ->
                criticalRepositoryNotFoundByDependencyLocatorErrorICityEither.fold(
                    createErrorResponse, createSuccessResponse)
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
