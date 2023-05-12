package com.heyrudy.mybatissample.gateway.store.spring.relational;

import com.heyrudy.mybatissample.gateway.store.spring.relational.config.PostgresContainerInit;
import com.heyrudy.mybatissample.gateway.store.spring.relational.entity.CityEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {PostgresContainerInit.class})
@Testcontainers
class CityEntityRepositoryIntegrationTest {

    @Autowired
    private CityRepository repository;

    @Test
    @DisplayName("Test postgresql container liveness")
    void postgres_container_liveness() {
        // ARRANGE - precondition or setup

        // ACT - action or behavior that we are going test

        // ASSERT - verify the result or output using assert statements
        assertThat(PostgresContainerInit.postgres11Container.isRunning())
                .isTrue();
    }

    @Test
    @DisplayName("Test update city's state by id from liquibase initialised")
    @Transactional
    void givenCitiesInDB_WhenUpdateCityState_ThenSuccess() {
        // ARRANGE - precondition or setup
        String expectedState = "Haut-De-Seine92";
        long expectedId = 5L;

        // ACT - action or behavior that we are going test
        repository.updateCityById(expectedId, expectedState);

        // ASSERT - verify the result or output using assert statements
        assertThat(repository.findById(expectedId))
                .isPresent()
                .hasValueSatisfying(actual -> assertThat(actual)
                        .extracting(CityEntity::getState)
                        .isEqualTo(expectedState)
                );
    }
}
