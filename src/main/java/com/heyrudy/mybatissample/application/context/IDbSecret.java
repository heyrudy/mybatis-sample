package com.heyrudy.mybatissample.application.context;

import com.heyrudy.mybatissample.application.context.IDbSecret.DbSecret;
import com.heyrudy.mybatissample.application.context.IDbSecret.H2DbSecret;
import com.heyrudy.mybatissample.application.context.IDbSecret.MockedDbSecret;
import org.springframework.boot.context.properties.ConfigurationProperties;

public sealed interface IDbSecret
    permits DbSecret
    , H2DbSecret
    , MockedDbSecret {

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

    record DbSecret(
        String host,
        int port,
        String protocol,
        String schema,
        String username,
        String password) implements IDbSecret {

        private static final IDbSecretValidator DB_SECRET_VALIDATOR =
            IDbSecretValidator.INSTANCE;


        public DbSecret(
            String host,
            int port,
            String protocol,
            String schema,
            String username,
            String password) {
            this.protocol =
                DB_SECRET_VALIDATOR.validateNonEmpty(protocol, "db.secret.protocol");
            this.host =
                DB_SECRET_VALIDATOR.validateNonEmpty(host, "db.secret.host");
            this.port =
                DB_SECRET_VALIDATOR.validatePort(port, "db.secret.port");
            this.schema =
                DB_SECRET_VALIDATOR.validateNonEmpty(schema, "db.secret.schema");
            this.username =
                DB_SECRET_VALIDATOR.validateNonEmpty(username, "db.secret.username");
            this.password =
                DB_SECRET_VALIDATOR.validateNonEmpty(password, "db.secret.password");
        }

        @Override
        public String driverClassName() {
            return "";
        }

        @Override
        public String getJdbcUrl() {
            return "%s://%s/%s".formatted(protocol, host, schema);
        }
    }

    @ConfigurationProperties(prefix = "h2.db.secret")
    record H2DbSecret(
        String driverClassName,
        String host,
        String protocol,
        String schema,
        String username,
        String password) implements IDbSecret {

        private static final IDbSecretValidator DB_SECRET_VALIDATOR =
            IDbSecretValidator.INSTANCE;

        public H2DbSecret(
            String driverClassName,
            String host,
            String protocol,
            String schema,
            String username,
            String password) {
            this.driverClassName =
                DB_SECRET_VALIDATOR.validateNonEmpty(
                    driverClassName, "h2.db.secret.driver-class-name");
            this.protocol =
                DB_SECRET_VALIDATOR.validateNonEmpty(
                    protocol, "h2.db.secret.protocol");
            this.host =
                DB_SECRET_VALIDATOR.validateNonEmpty(
                    host, "h2.db.secret.host");
            this.schema =
                DB_SECRET_VALIDATOR.validateNonEmpty(
                    schema, "h2.db.secret.schema");
            this.username =
                DB_SECRET_VALIDATOR.validateNonEmpty(
                    username, "h2.db.secret.username");
            this.password = password;
        }

        @Override
        public int port() {
            return 0;
        }

        @Override
        public String getJdbcUrl() {
            return "%s:%s".formatted(protocol, host);
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