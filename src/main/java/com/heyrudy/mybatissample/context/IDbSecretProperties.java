package com.heyrudy.mybatissample.context;

import com.heyrudy.mybatissample.context.IDbSecretProperties.MockedDbSecretProperties;

public sealed interface IDbSecretProperties
    permits DbSecretProperties,
    MockedDbSecretProperties {

    String getProtocol();

    String getHost();

    int getPort();

    String getSchema();

    String getUsername();

    char[] getPassword();

    String getJdbcUrl();

    void clearPassword();

    final class MockedDbSecretProperties implements IDbSecretProperties {

        @Override
        public String getProtocol() {
            return "jdbc:postgresql";
        }

        @Override
        public String getHost() {
            return "localhost";
        }

        @Override
        public int getPort() {
            return 5432;
        }

        @Override
        public String getSchema() {
            return "testdb";
        }

        @Override
        public String getUsername() {
            return "pg_user";
        }

        @Override
        public char[] getPassword() {
            return new char[0];
        }

        @Override
        public String getJdbcUrl() {
            return "%s://%s:%d/%s".formatted(getProtocol(), getHost(), getPort(), getSchema());
        }

        @Override
        public void clearPassword() {

        }
    }

    enum IDbSecretPropertiesValidator {
        INSTANCE;

        public String validateNonEmpty(String value, String propertyName) {
            if (value == null || value.trim().isEmpty()) {
                throw new IllegalArgumentException(
                    "Property '%s' cannot be null or empty".formatted(propertyName));
            }
            return value.trim();
        }

        public char[] validateNonEmpty(char[] value, String propertyName) {
            if (value == null || String.valueOf(value).trim().isEmpty()) {
                throw new IllegalArgumentException(
                    "Property '%s' cannot be null or empty".formatted(propertyName));
            }
            return String.valueOf(value).trim().toCharArray();
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