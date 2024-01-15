package com.heyrudy.mybatissample.gateway.store.spring.relational.config;

import org.testcontainers.containers.PostgreSQLContainer;

public class Postgres11Container extends PostgreSQLContainer<Postgres11Container> {

    public static final Integer POSTGRESQL_PORT = PostgreSQLContainer.POSTGRESQL_PORT;
    private static final Postgres11Container POSTGRES_11_CONTAINER = new Postgres11Container();

    private Postgres11Container() {
        super(ContainerConfig.DB_DOCKER_IMAGE_NAME);
    }

    public static Postgres11Container getInstance() {
        return POSTGRES_11_CONTAINER.withUsername(ContainerConfig.DB_USER_NAME)
            .withPassword(ContainerConfig.DB_USER_PASSWORD)
            .withDatabaseName(ContainerConfig.DB_SCHEMA_NAME);
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void stop() {
        // Do nothing. This is a per-test instance. Let JVM handle this operation.
    }

    static class ContainerConfig {

        public static final String DB_DOCKER_IMAGE_NAME = "postgres:11";
        public static final String DB_SCHEMA_NAME = "demodb";
        public static final String DB_USER_NAME = "postgres";
        public static final String DB_USER_PASSWORD = "mysecretpassword";
    }
}
