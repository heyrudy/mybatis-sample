package com.heyrudy.mybatissample.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heyrudy.mybatissample.entrypoint.rest.dto.CityCriteriaDTO;
import com.heyrudy.mybatissample.entrypoint.rest.dto.CityResponseDTO;
import com.heyrudy.mybatissample.entrypoint.rest.CityController;
import com.heyrudy.mybatissample.core.service.CityService;
import com.heyrudy.mybatissample.core.model.city.City;
import io.vavr.control.Either;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static java.lang.String.format;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = {
                CityController.class
        }
)
class CityEntityControllerWebLayerTest {

    private static final String CITIES_API_V_1_ENDPOINT = "/api/v1/cities";

    @MockBean
    private CityService restService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("create city endpoint")
    void createCity() throws Exception {
        // ARRANGE - precondition or setup
        CityResponseDTO cityResponseDto = CityResponseDTO.builder().build();
        willDoNothing().given(restService).createCity(any(City.class));
        MockHttpServletRequestBuilder postResult = MockMvcRequestBuilders.post(CITIES_API_V_1_ENDPOINT);

        // ACT - action or behavior that we are going test
        ResultActions actualPerformResult = mockMvc.perform(
                postResult.contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cityResponseDto))
        );

        // ASSERT - verify the result or output using assert statements
        actualPerformResult.andExpect(status().isCreated());
    }

    @Test
    @DisplayName("find city by its id first usecase")
    void findCities() throws Exception {
        // ARRANGE - precondition or setup
        Matcher<String> matcher = Matchers.containsString("[]");
        List<City> citiesDto = List.of();
        given(restService.findCities()).willReturn(citiesDto);

        // ACT - action or behavior that we are going test
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(CITIES_API_V_1_ENDPOINT);

        // ASSERT - verify the result or output using assert statements
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().string(matcher));
    }

    @Test
    @DisplayName("find a city by its id second usecase")
    void findCityById() throws Exception {
        // ARRANGE - precondition or setup
        City city = City.builder().build();
        Matcher<String> matcher = Matchers.containsString("{\"name\":null,\"state\":null,\"country\":null}");
        given(restService.findCityById(isA(CityCriteriaDTO.class))).willReturn(Either.right(city));

        // ACT - action or behavior that we are going test
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(format("%s/{cityId}", CITIES_API_V_1_ENDPOINT), 12345678987654321L);

        // ASSERT - verify the result or output using assert statements
        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().string(matcher));
    }
}
