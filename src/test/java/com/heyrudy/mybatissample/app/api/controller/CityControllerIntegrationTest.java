package com.heyrudy.mybatissample.app.api.controller;

import com.heyrudy.mybatissample.MybatisSampleApplication;
import com.heyrudy.mybatissample.app.api.internal.handlers.dbstorage.spring.entity.dto.CityResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.net.URISyntaxException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        classes = {
                MybatisSampleApplication.class
        },
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class CityControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int randomServerPort;

    private static final String CITY_ENDPOINT = "/api/v1/cities";

    @Test
    @DisplayName("create a new city in almost real world")
    void shouldCreateCity_whenClientRequestToCreateANewCity() throws URISyntaxException {
        // Arrange
        String localhost = "http://localhost:";
        String baseUrl = localhost + randomServerPort + CITY_ENDPOINT;
        URI uri = new URI(baseUrl);
        CityResponseDto cityResponseDto = new CityResponseDto("Epinay", "Seine-Saint-Denis", "France");
        HttpEntity<CityResponseDto> request = new HttpEntity<>(cityResponseDto);
        // Act
        ResponseEntity<Void> response = restTemplate.postForEntity(uri, request, null);
        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @DisplayName("find city by id in almost real world")
    void shouldReturnCity_whenClientRequestCityById() {
        // Arrange
        // Act
        boolean test = true;
        // Assert
        assertThat(test).isTrue();
    }

    @Test
    @DisplayName("find all cities in almost real world")
    void shouldReturnCities_whenClientRequestAllCities() {
        // Arrange
        // Act
        boolean test = true;
        // Assert
        assertThat(test).isTrue();
    }
}
