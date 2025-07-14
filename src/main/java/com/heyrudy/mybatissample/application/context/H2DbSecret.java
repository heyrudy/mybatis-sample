package com.heyrudy.mybatissample.application.context;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "h2.db.secret")
public record H2DbSecret(
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

    public static H2DbSecretBuilder builder() {
        return new H2DbSecretBuilder();
    }

    @Override
    public int port() {
        return 0;
    }

    @Override
    public String getJdbcUrl() {
        return "%s:%s".formatted(protocol, host);
    }

    public static class H2DbSecretBuilder {

        private String driverClassName;
        private String protocol;
        private String host;
        private String schema;
        private String username;
        private String password;

        public H2DbSecretBuilder() {
            super();
        }

        public static H2DbSecretBuilder builder() {
            return new H2DbSecretBuilder();
        }

        public H2DbSecretBuilder driverClassName(String driverClassName) {
            this.driverClassName = driverClassName;
            return this;
        }

        public H2DbSecretBuilder protocol(String protocol) {
            this.protocol = protocol;
            return this;
        }

        public H2DbSecretBuilder host(String host) {
            this.host = host;
            return this;
        }

        public H2DbSecretBuilder schema(String schema) {
            this.schema = schema;
            return this;
        }

        public H2DbSecretBuilder username(String username) {
            this.username = username;
            return this;
        }

        public H2DbSecretBuilder password(String password) {
            this.password = password;
            return this;
        }

        public H2DbSecret build() {
            return new H2DbSecret(
                driverClassName,
                protocol,
                host,
                schema,
                username,
                password
            );
        }
    }
}