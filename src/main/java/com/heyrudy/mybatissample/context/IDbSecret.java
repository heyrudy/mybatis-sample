package com.heyrudy.mybatissample.context;

import com.heyrudy.mybatissample.context.IDbSecret.MockedDbSecret;

public sealed interface IDbSecret
    permits DbSecret, H2DbSecret, MockedDbSecret {

    String driverClassName();

    String protocol();

    String host();

    int port();

    String schema();

    String username();

    String password();

    String getJdbcUrl();

    final class MockedDbSecret implements IDbSecret {

        @Override
        public String driverClassName() {
            return "org.h2.Driver";
        }

        @Override
        public String protocol() {
            return "jdbc:h2:file";
        }

        @Override
        public String host() {
            return "";
        }

        @Override
        public int port() {
            return 1;
        }

        @Override
        public String schema() {
            return "./data/demodb;MODE=PostgreSQL;DATABASE_TO_LOWER=TRUE";
        }

        @Override
        public String username() {
            return "SA";
        }

        @Override
        public String password() {
            return "";
        }

        @Override
        public String getJdbcUrl() {
            return "%s:%s".formatted(protocol(), schema());
        }
    }

    enum IDbSecretValidator {
        INSTANCE;

        public String validateNonEmpty(String value, String propertyName) {
            if (value == null || value.trim().isEmpty()) {
                throw new IllegalArgumentException(
                    "Property '%s' cannot be null or empty".formatted(propertyName));
            }
            return value.trim();
        }

        public int validatePort(int port, String propertyName) {
            if (port < 1 || port > 65535) {
                throw new IllegalArgumentException(
                    "Property '%s' must be between 1 and 65535, got: %d"
                        .formatted(propertyName, port));
            }
            return port;
        }

        public int validatePositive(int value, String propertyName) {
            if (value <= 0) {
                throw new IllegalArgumentException(
                    "Property '%s' must be positive, got: %d".formatted(propertyName, value));
            }
            return value;
        }
    }
}