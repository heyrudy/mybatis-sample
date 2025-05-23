package com.heyrudy.mybatissample.context;

import java.util.Arrays;

public final class DbSecretProperties implements IDbSecretProperties {

    private static final IDbSecretPropertiesValidator DB_SECRET_PROPERTIES_VALIDATOR =
        IDbSecretPropertiesValidator.INSTANCE;

    private final String protocol;
    private final String host;
    private final int port;
    private final String schema;
    private final String username;
    private final char[] password;

    public DbSecretProperties(DbSecretPropertiesBuilder builder) {
        this.protocol =
            DB_SECRET_PROPERTIES_VALIDATOR.validateNonEmpty(builder.protocol, "db.secret.protocol");
        this.host =
            DB_SECRET_PROPERTIES_VALIDATOR.validateNonEmpty(builder.host, "db.secret.host");
        this.port =
            DB_SECRET_PROPERTIES_VALIDATOR.validatePort(builder.port, "db.secret.port");
        this.schema =
            DB_SECRET_PROPERTIES_VALIDATOR.validateNonEmpty(builder.schema, "db.secret.schema");
        this.username =
            DB_SECRET_PROPERTIES_VALIDATOR.validateNonEmpty(builder.username, "db.secret.username");
        this.password =
            DB_SECRET_PROPERTIES_VALIDATOR.validateNonEmpty(builder.password, "db.secret.password");
    }

    public static DbSecretPropertiesBuilder builder() {
        return new DbSecretPropertiesBuilder();
    }

    @Override
    public String getProtocol() {
        return protocol;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public String getSchema() {
        return schema;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public char[] getPassword() {
        return password;
    }

    @Override
    public String getJdbcUrl() {
        return "%s://%s:%s/%s".formatted(protocol, host, port, schema);
    }

    @Override
    public void clearPassword() {
        if (password != null) {
            Arrays.fill(password, '\0');
        }
    }

    public static class DbSecretPropertiesBuilder {

        private String protocol;
        private String host;
        private int port;
        private String schema;
        private String username;
        private char[] password;

        public DbSecretPropertiesBuilder() {
            super();
        }

        public static DbSecretPropertiesBuilder builder() {
            return new DbSecretPropertiesBuilder();
        }

        public DbSecretPropertiesBuilder protocol(String protocol) {
            this.protocol = protocol;
            return this;
        }

        public DbSecretPropertiesBuilder host(String host) {
            this.host = host;
            return this;
        }

        public DbSecretPropertiesBuilder port(int port) {
            this.port = port;
            return this;
        }

        public DbSecretPropertiesBuilder schema(String schema) {
            this.schema = schema;
            return this;
        }

        public DbSecretPropertiesBuilder username(String username) {
            this.username = username;
            return this;
        }

        public DbSecretPropertiesBuilder password(char[] password) {
            this.password = password;
            return this;
        }

        public DbSecretProperties build() {
            return new DbSecretProperties(this);
        }
    }
}