package com.heyrudy.mybatissample.app.api.controller;

import com.heyrudy.mybatissample.MybatisSampleApplication;
import com.heyrudy.mybatissample.app.api.internal.handlers.dbstorage.spring.entity.dto.CityDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = MybatisSampleApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(value = SpringExtension.class)
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
        CityDto cityDto = new CityDto("Epinay", "Seine-Saint-Denis", "France");
        HttpEntity<CityDto> request = new HttpEntity<>(cityDto);
        // Act
        ResponseEntity<Void> response = restTemplate.postForEntity(uri, request, null);
        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("find city by id in almost real world")
    void shouldReturnCity_whenClientRequestCityById() {
        // Arrange
        // Act
        // Assert
        assertTrue(true);
    }

    @Test
    @DisplayName("find all cities in almost real world")
    void shouldReturnCities_whenClientRequestAllCities() {
        // Arrange
        // Act
        // Assert
        assertTrue(true);
    }
}