package com.heyrudy.mybatissample.domain;

public interface DomainErrorModule {

    sealed interface DomainError
        permits MissingCriticalDependencyError
        , MissingCriticalProgramHandlerError
        , DomainRepositoryError
        , DomainServiceSPIError
        , DomainServiceAPIError {

    }

    sealed interface MissingCriticalDependencyError
        extends DomainError
        permits MissingCriticalSecretError
        , MissingCriticalConfigError
        , MissingCriticalDependencyError.CriticalRepositoryNotFoundByDependencyLocatorError {

        String message();

        record CriticalRepositoryNotFoundByDependencyLocatorError(String message)
            implements MissingCriticalDependencyError {

            public RuntimeException toException() {
                return new RuntimeException(this.message);
            }

            public static class ErrorMessage {

                public static final String CRITICAL_REPOSITORY_NOT_FOUND_FOR_KEY =
                    "No critical repository found for key: %s";
            }
        }
    }

    sealed interface DomainServiceSPIError
        extends DomainError
        permits DomainServiceSPIError.PDFDocumentCreationError {

        record PDFDocumentCreationError(String message)
            implements DomainServiceSPIError {

        }
    }

    sealed interface DomainServiceAPIError
        extends DomainError
        permits DomainServiceAPIError.CitiesNotFoundError
        , DomainServiceAPIError.CityNotFoundError
        , DomainServiceAPIError.CityNotSavedError {

        String message();

        record CityNotFoundError(String message)
            implements DomainServiceAPIError {

            public static class ErrorMessage {

                public static final String CITY_NOT_FOUND =
                    "City with id %d was not found";
            }

            public static class SuccessMessage {

                public static final String CITY_FOUND =
                    "A city with id {} is found";
            }
        }

        record CitiesNotFoundError(String message)
            implements DomainServiceAPIError {

        }

        record CityNotSavedError(String message)
            implements DomainServiceAPIError {

            public static class ErrorMessage {

                public static final String CITY_NOT_SAVED =
                    """
                        City with details:
                        {}
                        was not saved""";
            }

            public static class SuccessMessage {

                public static final String CITY_SAVED =
                    "A city  is saved with id {}";
            }
        }
    }

    sealed interface MissingCriticalProgramHandlerError
        extends DomainError
        permits MissingCriticalProgramHandlerError.MissingCriticalCityProgramHandlerError
        , MissingCriticalProgramHandlerError.MissingNonCriticalAuditProgramHandlerError {

        String message();

        record MissingCriticalCityProgramHandlerError(String message)
            implements MissingCriticalProgramHandlerError {

            public RuntimeException toException() {
                return new RuntimeException(this.message);
            }
        }

        record MissingNonCriticalAuditProgramHandlerError(String message)
            implements MissingCriticalProgramHandlerError {

            public RuntimeException toException() {
                return new RuntimeException(this.message);
            }
        }
    }
}