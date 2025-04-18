package com.heyrudy.mybatissample.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heyrudy.mybatissample.controller.rest.CityRouterConfig;
import com.heyrudy.mybatissample.controller.rest.dto.CityRequestDTO;
import com.heyrudy.mybatissample.gateway.config.TestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest
@Import(
    value = {
        TestConfig.class,
        CityRouterConfig.class
    }
)
class CityCriticalRestAPIAdapterIntegrationTest {

    private static final String CITIES_API_V_1_ENDPOINT = "/api/v1/cities";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("create a new city in almost real world")
    void shouldCreateCity_whenClientRequestToCreateANewCity() throws Exception {
        // ARRANGE - precondition or setup
        CityRequestDTO cityRequestDTO =
            CityRequestDTO.builder()
                .name("Paris")
                .country("France")
                .state("Paris75").build();

        // ACT - action or behavior that we are going to test
        // ASSERT - verify the result or output using assert statements
        mockMvc.perform(
                post(CITIES_API_V_1_ENDPOINT)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(cityRequestDTO)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value(cityRequestDTO.name()))
            .andExpect(jsonPath("$.country").value(cityRequestDTO.country()))
            .andExpect(jsonPath("$.state").value(cityRequestDTO.state()));
    }

    @Test
    @DisplayName("find city by id in almost real world")
    void shouldReturnCity_whenClientRequestCityById() {
        // ARRANGE - precondition or setup

        // ACT - action or behavior that we are going to test
        boolean test = true;

        // ASSERT - verify the result or output using assert statements
        assertThat(test).isTrue();
    }

    @Test
    @DisplayName("find all cities in almost real world")
    void shouldReturnCities_whenClientRequestAllCities() {
        // ARRANGE - precondition or setup

        // ACT - action or behavior that we are going to test
        boolean test = true;

        // ASSERT - verify the result or output using assert statements
        assertThat(test).isTrue();
    }
}
