package com.heyrudy.mybatissample.application;

import com.heyrudy.mybatissample.application.APIErrorModule.ApiErrorResponse.ApiErrorResponseMutatorOptions;
import com.heyrudy.mybatissample.application.context.AppScopedDependencyLocator;
import com.heyrudy.mybatissample.domain.DomainErrorModule;
import com.heyrudy.mybatissample.gateway.file.PDFResourceModule;
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

public interface CityRestAPIModule
    extends CityValidatorModule,
    CityMapperModule,
    CityInteractorModule,
    PDFResourceModule,
    APIErrorModule,
    DomainErrorModule {

    enum CityCriticalRestAPIAdapter {
        INSTANCE;

        public static final Logger LOGGER = LoggerFactory.getLogger(
            CityCriticalRestAPIAdapter.class);

        private static final CityRequestDTOValidator CITY_REQUEST_DTO_VALIDATOR = CityRequestDTOValidator.INSTANCE;
        private static final CityCriteriaValidator CITY_CRITERIA_VALIDATOR = CityCriteriaValidator.INSTANCE;
        private static final CityRequestMapper CITY_REQUEST_MAPPER = CityRequestMapper.INSTANCE;
        private static final CityResponseMapper CITY_RESPONSE_MAPPER = CityResponseMapper.INSTANCE;
        private static final CreateCityInteractor CREATE_CITY_INTERACTOR = CreateCityInteractor.INSTANCE;
        private static final FindCityByIdInteractor FIND_CITY_BY_ID_INTERACTOR = FindCityByIdInteractor.INSTANCE;
        private static final FindCitiesInteractor FIND_CITIES_INTERACTOR = FindCitiesInteractor.INSTANCE;
        private static final CreatePdfUtil CREATE_PDF_UTIL = CreatePdfUtil.INSTANCE;
        // A reader that always returns a specific error value
        private static final Function<String, Reader<AppScopedDependencyLocator, Either<DomainServiceAPIError, ICity>>> CITY_NEVER_SAVED_PATH =
            errMsg -> __ -> Either.left(
                new DomainServiceAPIError.CityNotSavedError(errMsg));
        private static final Function<String, Reader<AppScopedDependencyLocator, Either<DomainServiceAPIError, ICity>>> CITY_NEVER_FOUND_PATH =
            errMsg -> __ -> Either.left(
                new DomainServiceAPIError.CityNotFoundError(errMsg));
        private static final Function<Validation<String, CityCriteriaDetails>, Reader<AppScopedDependencyLocator, Either<DomainServiceAPIError, ICity>>> FAILED_VALIDATION_OR_FIND_CITY_BY_ID_PATH =
            stringCityCriteriaDetailsValidation ->
                stringCityCriteriaDetailsValidation.fold(
                    CITY_NEVER_FOUND_PATH, FIND_CITY_BY_ID_INTERACTOR::execute);
        // Define a function to validate a DTO
        private static final Function<CityRequestDTO, Either<String, CityRequestDTO>> VALIDATE_CITY_POST_REQUEST_DTO_PATH =
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
        private static final Function<Either<String, CityRequestDTO>, Either<String, CityRequestDTO>> PARSE_THEN_VALIDATE_CITY_POST_REQUEST_DTO_PATH =
            parsedBodyEither -> parsedBodyEither.flatMap(VALIDATE_CITY_POST_REQUEST_DTO_PATH);
        // Define function to transform DTO to model
        private static final Function<CityRequestDTO, ICity> MAP_TO_CITY_PATH =
            CITY_REQUEST_MAPPER::toModel;
        private static final Function<Either<String, CityRequestDTO>, Reader<AppScopedDependencyLocator, Either<DomainServiceAPIError, ICity>>> FAILED_TO_CREATE_OR_CREATE_CITY_PATH =
            validatedDtoEither ->
                validatedDtoEither.fold(
                    CITY_NEVER_SAVED_PATH,
                    MAP_TO_CITY_PATH.andThen(CREATE_CITY_INTERACTOR::execute));

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
            Function<DomainServiceAPIError, ServerResponse> createErrorResponse =
                cityNotSavedError ->
                    ServerResponse.status(HttpStatus.FAILED_DEPENDENCY)
                        .body(cityNotSavedError.message());
            Function<ICity, ServerResponse> createSuccessResponse =
                iCity ->
                    ServerResponse.status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(CITY_RESPONSE_MAPPER.toDto(iCity));
            // Compose operations with flatMap to explicitly avoid apply
            return parseBodyReader
                .map(PARSE_THEN_VALIDATE_CITY_POST_REQUEST_DTO_PATH)
                .flatMap(FAILED_TO_CREATE_OR_CREATE_CITY_PATH)
                .map(criticalRepositoryNotFoundByDependencyLocatorErrorICityEither ->
                    criticalRepositoryNotFoundByDependencyLocatorErrorICityEither.fold(
                        createErrorResponse, createSuccessResponse));
        }

        /**
         * @return HTTP Response with all cities fetched from the database
         */
        public Reader<AppScopedDependencyLocator, ServerResponse> findCities() {
            Function<DomainServiceAPIError, ServerResponse> createErrorResponse =
                domainServiceAPIError ->
                    ServerResponse.status(HttpStatus.FAILED_DEPENDENCY)
                        .body(domainServiceAPIError.message());
            Function<List<ICity>, ServerResponse> createSuccessResponse =
                iCityList -> {
                    LOGGER.info("All cities were found");
                    return ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(iCityList.stream()
                            .map(CITY_RESPONSE_MAPPER::toDto)
                            .toList());
                };
            // Compose operations with flatMap to explicitly avoid apply
            return FIND_CITIES_INTERACTOR.execute()
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
            Reader<AppScopedDependencyLocator, Either<DomainServiceAPIError, ICity>> findCityByIdReader =
                validateIdReader
                    .flatMap(FAILED_VALIDATION_OR_FIND_CITY_BY_ID_PATH);
            Function<DomainServiceAPIError, ServerResponse> createErrorResponse =
                cityNotFoundError -> {
                    LOGGER.error(cityNotFoundError.message());
                    return ServerResponse.badRequest()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(ApiErrorResponse.with(
                            ApiErrorResponseMutatorOptions.INSTANCE.reason(
                                cityNotFoundError.message())));
                };
            Function<ICity, ServerResponse> createSuccessResponse =
                iCity -> {
                    LOGGER.info(
                        DomainServiceAPIError.CityNotFoundError.SuccessMessage.CITY_FOUND, id);
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
            return CREATE_PDF_UTIL.createPdf()
                .fold(
                    pdfDocumentCreationError ->
                        ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(pdfDocumentCreationError.message()),
                    bytes ->
                        ServerResponse.ok()
                            .headers(httpHeaders ->
                                httpHeaders.add("content-disposition",
                                    "attachment; filename=%s".formatted("cityReport.pdf")))
                            .contentType(MediaType.APPLICATION_OCTET_STREAM)
                            .body(new ByteArrayResource(bytes))
                );
        }
    }
}
