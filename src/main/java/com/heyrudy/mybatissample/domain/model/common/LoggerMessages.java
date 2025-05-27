package com.heyrudy.mybatissample.domain.model.common;

public interface LoggerMessages {

    class ApiLoggerMessages {

        // API execution phase messages
        public static final String DEBUT_TRAITEMENT_API = "Début: ";
        public static final String FIN_TRAITEMENT_API = "Fin: ";

        // Error messages
        public static final String ERREUR = "Erreur";

        private ApiLoggerMessages() {
            throw new AssertionError("This class cannot be instantiated");
        }
    }
}