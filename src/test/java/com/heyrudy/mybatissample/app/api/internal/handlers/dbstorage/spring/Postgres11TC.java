package com.heyrudy.mybatissample.app.api.internal.handlers.dbstorage.spring;

import org.testcontainers.containers.PostgreSQLContainer;

public class Postgres11TC extends PostgreSQLContainer<Postgres11TC> {

    public static final Integer POSTGRESQL_PORT = PostgreSQLContainer.POSTGRESQL_PORT;
    private static final Postgres11TC TC = new Postgres11TC();

    private Postgres11TC() {
        super(ContainerConfig.DB_DOCKER_IMAGE_NAME);
    }

    public static Postgres11TC getInstance() {
        return TC.withUsername(ContainerConfig.DB_USER_NAME)
                .withPassword(ContainerConfig.DB_USER_PASSWORD)
                .withDatabaseName(ContainerConfig.DB_SCHEMA_NAME);
    }

    @Override
    public void start() {
        super.start();
    }

    @Override
    public void stop() {
        // do nothing. This is a per-test instance. Let JVM handle this operation.
    }

    static class ContainerConfig {
        public static final String DB_DOCKER_IMAGE_NAME = "postgres:11";
        public static final String DB_SCHEMA_NAME = "demodb";
        public static final String DB_USER_NAME = "postgres";
        public static final String DB_USER_PASSWORD = "mysecretpassword";
    }
}
