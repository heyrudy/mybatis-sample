package com.heyrudy.mybatissample.context;

public record DbSecret(
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

    public static DbSecretBuilder builder() {
        return new DbSecretBuilder();
    }

    @Override
    public String driverClassName() {
        return "";
    }

    @Override
    public String getJdbcUrl() {
        return "%s://%s/%s".formatted(protocol, host, schema);
    }

    public static class DbSecretBuilder {

        private String protocol;
        private String host;
        private int port;
        private String schema;
        private String username;
        private String password;

        public DbSecretBuilder() {
            super();
        }

        public DbSecretBuilder protocol(String protocol) {
            this.protocol = protocol;
            return this;
        }

        public DbSecretBuilder host(String host) {
            this.host = host;
            return this;
        }

        public DbSecretBuilder port(int port) {
            this.port = port;
            return this;
        }

        public DbSecretBuilder schema(String schema) {
            this.schema = schema;
            return this;
        }

        public DbSecretBuilder username(String username) {
            this.username = username;
            return this;
        }

        public DbSecretBuilder password(String password) {
            this.password = password;
            return this;
        }

        public DbSecret build() {
            return new DbSecret(
                host,
                port,
                protocol,
                schema,
                username,
                password
            );
        }
    }
}