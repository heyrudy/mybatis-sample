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
import com.heyrudy.mybatissample.domain.model.common.CityCriteriaDetails;
import com.heyrudy.mybatissample.domain.model.error.CityNotFoundError;
import com.heyrudy.mybatissample.domain.model.error.CityNotFoundError.SuccessMessage;
import com.heyrudy.mybatissample.domain.model.error.CityNotSavedError;
import com.heyrudy.mybatissample.domain.model.error.CriticalRepositoryNotFoundByDependencyLocatorError;
import com.heyrudy.mybatissample.gateway.file.pdf.CreatePdfUtil;
import cyclops.control.Reader;
import io.vavr.collection.Seq;
import io.vavr.control.Either;
import io.vavr.control.Try;
import io.vavr.control.Validation;
import java.util.List;
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

    private static final CityRequestDTOValidator CITY_REQUEST_DTO_VALIDATOR = CityRequestDTOValidator.INSTANCE;
    private static final CityCriteriaValidator CITY_CRITERIA_VALIDATOR = CityCriteriaValidator.INSTANCE;
    private static final CityRequestMapper CITY_REQUEST_MAPPER = CityRequestMapper.INSTANCE;
    private static final CityResponseMapper CITY_RESPONSE_MAPPER = CityResponseMapper.INSTANCE;
    private static final CreateCityAPI CREATE_CITY_API = CreateCityAPI.INSTANCE;
    private static final FindCityByIdAPI FIND_CITY_BY_ID_API = FindCityByIdAPI.INSTANCE;
    private static final FindCitiesAPI FIND_CITIES_API = FindCitiesAPI.INSTANCE;
    private static final CreatePdfUtil CREATE_PDF_UTIL = CreatePdfUtil.INSTANCE;
    // A reader that always returns a specific error value
    private static final Function<String, Reader<AppScopedDependencyLocator, Either<CityNotSavedError, ICity>>> CONSTANT_CITY_NOY_SAVED_ERROR_READER =
        errMsg ->
            __ -> Either.left(
                new CityNotSavedError(errMsg));
    private static final Function<String, Reader<AppScopedDependencyLocator, Either<CityNotFoundError, ICity>>> CONSTANT_CITY_NOY_FOUND_ERROR_READER =
        errMsg ->
            __ -> Either.left(new CityNotFoundError(errMsg));
    private static final Function<Validation<String, CityCriteriaDetails>, Reader<AppScopedDependencyLocator, Either<CityNotFoundError, ICity>>> EITHER_FAILED_VALIDATION_OR_FIND_CITY_BY_ID =
        stringCityCriteriaDetailsValidation ->
            stringCityCriteriaDetailsValidation.fold(
                CONSTANT_CITY_NOY_FOUND_ERROR_READER, FIND_CITY_BY_ID_API::execute);
    // Define a function to validate a DTO
    private static final Function<CityRequestDTO, Either<String, CityRequestDTO>> VALIDATE_DTO =
        cityRequestDTO -> {
            Validation<Seq<String>, CityRequestDTO> cityRequestDTOValidation =
                CITY_REQUEST_DTO_VALIDATOR.validateCityRequestDTO(
                    cityRequestDTO.name(), cityRequestDTO.state(), cityRequestDTO.country());
            return cityRequestDTOValidation.isValid()
                ? Either.right(cityRequestDTO)
                : Either.left(
                    String.valueOf(cityRequestDTOValidation.toStream()
                        .reduce((a, b) -> a.equals(b) ? a : b)));
        };
    private static final Function<Either<String, CityRequestDTO>, Either<String, CityRequestDTO>> VALIDATE_PARSED_CITY_POST_REQUEST_BODY =
        parsedBodyEither -> parsedBodyEither.flatMap(VALIDATE_DTO);
    // Define function to transform DTO to model and execute create operation
    private static final Function<CityRequestDTO, Reader<AppScopedDependencyLocator, Either<CityNotSavedError, ICity>>> DTO_TO_CREATE_CITY =
        cityRequestDTO ->
            CREATE_CITY_API.execute(CITY_REQUEST_MAPPER.toModel(cityRequestDTO));
    private static final Function<Either<String, CityRequestDTO>, Reader<AppScopedDependencyLocator, Either<CityNotSavedError, ICity>>> EITHER_REPOSITORY_NOT_FOUND_ERROR_OR_CREATE_CITY_READER =
        validatedDtoEither ->
            validatedDtoEither.fold(CONSTANT_CITY_NOY_SAVED_ERROR_READER, DTO_TO_CREATE_CITY);

    /**
     * @param request city with all its details to persist in the database
     * @return HTTP Response with persisted city in the database
     */
    public Reader<AppScopedDependencyLocator, ServerResponse> createCity(
        ServerRequest request) {
        Reader<AppScopedDependencyLocator, Either<String, CityRequestDTO>> parseBodyReader =
            __ ->
                Try.of(() -> request.body(CityRequestDTO.class))
                    .toEither()
                    .mapLeft(Throwable::getMessage);
        Function<CityNotSavedError, ServerResponse> createErrorResponse =
            cityNotSavedError ->
                ServerResponse.status(HttpStatus.FAILED_DEPENDENCY)
                    .body(cityNotSavedError.getMessage());
        Function<ICity, ServerResponse> createSuccessResponse =
            iCity ->
                ServerResponse.status(HttpStatus.CREATED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(CITY_RESPONSE_MAPPER.toDto(iCity));
        // Compose operations with flatMap to explicitly avoid apply
        return parseBodyReader
            .map(VALIDATE_PARSED_CITY_POST_REQUEST_BODY)
            .flatMap(EITHER_REPOSITORY_NOT_FOUND_ERROR_OR_CREATE_CITY_READER)
            .map(criticalRepositoryNotFoundByDependencyLocatorErrorICityEither ->
                criticalRepositoryNotFoundByDependencyLocatorErrorICityEither.fold(
                    createErrorResponse, createSuccessResponse));
    }

    /**
     * @return HTTP Response with all cities fetched from the database
     */
    public Reader<AppScopedDependencyLocator, ServerResponse> findCities() {
        Function<CriticalRepositoryNotFoundByDependencyLocatorError, ServerResponse> createErrorResponse =
            criticalRepositoryNotFoundByDependencyLocatorError ->
                ServerResponse.status(HttpStatus.FAILED_DEPENDENCY)
                    .body(criticalRepositoryNotFoundByDependencyLocatorError.getMessage());
        Function<List<ICity>, ServerResponse> createSuccessResponse =
            iCityList -> {
                logger.info("All cities were found");
                return ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(iCityList.stream()
                        .map(CITY_RESPONSE_MAPPER::toDto)
                        .toList());
            };
        // Compose operations with flatMap to explicitly avoid apply
        return FIND_CITIES_API.execute()
            .map(criticalRepositoryNotFoundByDependencyLocatorErrorListEither ->
                criticalRepositoryNotFoundByDependencyLocatorErrorListEither.fold(
                    createErrorResponse, createSuccessResponse));
    }

    /**
     * @param request city's id to fetch from the database
     * @return HTTP Response with required information about a city
     */
    public Reader<AppScopedDependencyLocator, ServerResponse> findCityById(
        ServerRequest request) {
        String id = request.pathVariable("cityId");
        Reader<AppScopedDependencyLocator, Validation<String, CityCriteriaDetails>> validateIdReader =
            __ ->
                CITY_CRITERIA_VALIDATOR.validateCityCriteria(Long.parseLong(id));
        Reader<AppScopedDependencyLocator, Either<CityNotFoundError, ICity>> findCityByIdReader =
            validateIdReader
                .flatMap(EITHER_FAILED_VALIDATION_OR_FIND_CITY_BY_ID);
        Function<CityNotFoundError, ServerResponse> createErrorResponse =
            cityNotFoundError -> {
                logger.error(cityNotFoundError.getMessage());
                return ServerResponse.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(ApiErrorResponse.builder()
                        .reason(cityNotFoundError.getMessage())
                        .build());
            };
        Function<ICity, ServerResponse> createSuccessResponse =
            iCity -> {
                logger.info(SuccessMessage.CITY_FOUND_SUCCESS_MESSAGE, id);
                return ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(CITY_RESPONSE_MAPPER.toDto(iCity));
            };
        // Compose operations with flatMap to explicitly avoid apply
        return findCityByIdReader
            .map(cityNotFoundErrorICityEither ->
                cityNotFoundErrorICityEither.fold(createErrorResponse, createSuccessResponse));
    }

    public ServerResponse downloadCityPdfReport() {
        return ServerResponse.ok()
            .headers(httpHeaders ->
                httpHeaders.add("content-disposition",
                    "attachment; filename=%s".formatted("cityReport.pdf")))
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(new ByteArrayResource(CREATE_PDF_UTIL.createPdf()));
    }
}
