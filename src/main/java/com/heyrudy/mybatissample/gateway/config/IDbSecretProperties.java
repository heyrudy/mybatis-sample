package com.heyrudy.mybatissample.gateway.config;

import com.heyrudy.mybatissample.gateway.config.IDbSecretProperties.MockedDbSecretProperties;

public sealed interface IDbSecretProperties
    permits DbSecretProperties,
    MockedDbSecretProperties {

    String host();

    String port();

    String protocol();

    String schema();

    String username();

    char[] password();

    String getJdbcUrl();

    void clearPassword();

    final class MockedDbSecretProperties implements IDbSecretProperties {

        @Override
        public String host() {
            return "localhost";
        }

        @Override
        public String port() {
            return "5432";
        }

        @Override
        public String protocol() {
            return "jdbc:postgresql";
        }

        @Override
        public String schema() {
            return "testdb";
        }

        @Override
        public String username() {
            return "pg_user";
        }

        @Override
        public char[] password() {
            return new char[0];
        }

        @Override
        public String getJdbcUrl() {
            return "%s://%s:%s/%s".formatted(protocol(), host(), port(), schema());
        }

        @Override
        public void clearPassword() {

        }
    }
}