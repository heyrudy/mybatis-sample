package com.heyrudy.mybatissample.app.api.internal.handlers.dbstorage.spring;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

public class BaseIntegrationTestConfig {

    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(DBContainerConfig.DOCKER_IMAGE_NAME)
            .withUsername(DBContainerConfig.USER_NAME)
            .withPassword(DBContainerConfig.USER_PASSWORD)
            .withDatabaseName(DBContainerConfig.SCHEMA_NAME);

    @DynamicPropertySource
    static void postgresqlProperties(DynamicPropertyRegistry registry) {
        postgreSQLContainer.start();
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.liquibase.contexts", () -> "!prod");
    }

    static class DBContainerConfig {
        public static final String DOCKER_IMAGE_NAME = "postgres:11";
        public static final String SCHEMA_NAME = "demodb";
        public static final String USER_NAME = "postgres";
        public static final String USER_PASSWORD = "mysecretpassword";
    }
}
