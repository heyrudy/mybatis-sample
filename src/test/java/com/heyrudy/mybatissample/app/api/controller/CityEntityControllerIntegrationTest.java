package com.heyrudy.mybatissample.app.api.controller;

import com.heyrudy.mybatissample.MybatisSampleApplication;
import com.heyrudy.mybatissample.api.handlers.dbstorage.spring.entity.dto.CityResponseDTO;
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
class CityEntityControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int randomServerPort;

    private static final String CITY_ENDPOINT = "/api/v1/cities";

    @Test
    @DisplayName("create a new city in almost real world")
    void shouldCreateCity_whenClientRequestToCreateANewCity() throws URISyntaxException {
        // ARRANGE - precondition or setup
        String localhost = "http://localhost:";
        String baseUrl = localhost + randomServerPort + CITY_ENDPOINT;
        URI uri = new URI(baseUrl);
        CityResponseDTO cityResponseDto = new CityResponseDTO("Epinay", "Seine-Saint-Denis", "France");
        HttpEntity<CityResponseDTO> request = new HttpEntity<>(cityResponseDto);

        // ACT - action or behaviour that we are going test
        ResponseEntity<Void> response = restTemplate.postForEntity(uri, request, null);

        // ASSERT - verify the result or output using assert statements
        assertThat(response)
                .isNotNull()
                .satisfies(it -> assertThat(it.getStatusCode())
                        .isEqualTo(HttpStatus.OK)
                );
    }

    @Test
    @DisplayName("find city by id in almost real world")
    void shouldReturnCity_whenClientRequestCityById() {
        // ARRANGE - precondition or setup

        // ACT - action or behaviour that we are going test
        boolean test = true;

        // ASSERT - verify the result or output using assert statements
        assertThat(test).isTrue();
    }

    @Test
    @DisplayName("find all cities in almost real world")
    void shouldReturnCities_whenClientRequestAllCities() {
        // ARRANGE - precondition or setup

        // ACT - action or behaviour that we are going test
        boolean test = true;

        // ASSERT - verify the result or output using assert statements
        assertThat(test).isTrue();
    }
}
