package com.heyrudy.mybatissample.app.api.internal.handlers.dbstorage.spring;

import com.heyrudy.mybatissample.app.api.internal.handlers.dbstorage.spring.entity.City;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static com.heyrudy.mybatissample.app.api.internal.handlers.dbstorage.spring.ContainerInit.postgres11TC;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = {ContainerInit.class})
class CityRepositoryIntegrationTest {

    @Autowired
    private CityRepository repository;

    @Test
    @DisplayName("Test postgresql container liveness")
    void postgres_container_liveness() {
        // Arrange
        // Act
        // Assert
        assertThat(postgres11TC.isRunning())
                .isTrue();
    }

    @Test
    @DisplayName("Test update city's state by id from liquibase initialised")
    @Transactional
    void givenCitiesInDB_WhenUpdateCityState_ThenSuccess() {
        // Arrange
        String expectedState = "Haut-De-Seine92";
        long expectedId = 5L;
        // Act
        repository.updateCityById(expectedId, expectedState);
        // Assert
        assertThat(repository.findById(expectedId))
                .isPresent()
                .hasValueSatisfying(actual -> assertThat(actual)
                        .extracting(City::getState)
                        .isEqualTo(expectedState));
    }
}
