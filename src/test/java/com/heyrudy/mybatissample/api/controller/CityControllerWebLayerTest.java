package com.heyrudy.mybatissample.api.controller;

import static java.lang.String.format;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heyrudy.mybatissample.controller.rest.CityController;
import com.heyrudy.mybatissample.controller.rest.dto.CityResponseDTO;
import com.heyrudy.mybatissample.domain.api.ICreateCityInteractorAPI;
import com.heyrudy.mybatissample.domain.api.IFindCitiesInteractorAPI;
import com.heyrudy.mybatissample.domain.api.IFindCityByIdInteractorAPI;
import com.heyrudy.mybatissample.domain.model.city.FullCity;
import com.heyrudy.mybatissample.domain.model.common.CityCriteriaDTO;
import io.vavr.control.Either;
import java.util.List;
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

@WebMvcTest(
    controllers = {
        CityController.class
    }
)
class CityControllerWebLayerTest {

    private static final String CITIES_API_V_1_ENDPOINT = "/api/v1/cities";

    @MockBean
    private ICreateCityInteractorAPI ICreateCityInteractorAPI;
    @MockBean
    private IFindCityByIdInteractorAPI IFindCityByIdInteractorAPI;
    @MockBean
    private IFindCitiesInteractorAPI IFindCitiesInteractorAPI;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("create city endpoint")
    void createCity() throws Exception {
        // ARRANGE - precondition or setup
        CityResponseDTO cityResponseDto = CityResponseDTO.builder().build();
        willDoNothing().given(ICreateCityInteractorAPI).execute(isA(FullCity.class));
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post(
            CITIES_API_V_1_ENDPOINT);

        // ACT - action or behavior that we are going test
        ResultActions actualPerformResult = mockMvc.perform(
            requestBuilder.contentType(MediaType.APPLICATION_JSON)
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
        List<FullCity> citiesDto = List.of();
        given(IFindCitiesInteractorAPI.execute()).willReturn(citiesDto);

        // ACT - action or behavior that we are going test
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(
            CITIES_API_V_1_ENDPOINT);

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
        FullCity fullCity = FullCity.builder().build();
        Matcher<String> matcher = Matchers.containsString(
            "{\"name\":null,\"state\":null,\"country\":null}");
        given(IFindCityByIdInteractorAPI.execute(isA(CityCriteriaDTO.class))).willReturn(
            Either.right(fullCity));

        // ACT - action or behavior that we are going test
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(
            format("%s/{cityId}", CITIES_API_V_1_ENDPOINT), 12345678987654321L);

        // ASSERT - verify the result or output using assert statements
        mockMvc.perform(requestBuilder)
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().string(matcher));
    }
}
