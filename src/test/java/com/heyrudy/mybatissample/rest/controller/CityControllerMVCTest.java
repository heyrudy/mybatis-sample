package com.heyrudy.mybatissample.rest.controller;

import com.heyrudy.mybatissample.dto.CityDto;
import com.heyrudy.mybatissample.rest.service.CityService;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;

import static java.lang.String.format;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = {CityController.class})
@ExtendWith(SpringExtension.class)
class CityControllerMVCTest {

    @MockBean
    private CityService restService;
    @Autowired
    private MockMvc mockMvc;

    private static final String CITY_ENDPOINT = "/api/v1/cities";

    @Test
    @DisplayName("create city endpoint")
    void createCity() throws Exception {
        // Arrange
        CityDto cityDto = new CityDto();
        MockHttpServletRequestBuilder postResult = MockMvcRequestBuilders.post(CITY_ENDPOINT);
        // Act
        ResultActions actualPerformResult = mockMvc.perform(postResult.param("dto", String.valueOf(cityDto)));
        // Assert
        actualPerformResult.andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    @DisplayName("find city given its id endpoint")
    void findCities() throws Exception {
        // Arrange
        Matcher<String> matcher = Matchers.containsString("[]");
        ArrayList<CityDto> citiesDto = new ArrayList<>();
        when(restService.findCities()).thenReturn(citiesDto);
        // Act
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(CITY_ENDPOINT);
        // Assert
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().string(matcher));
    }

    @Test
    @DisplayName("find all cities endpoint")
    void findCityById() throws Exception {
        // Arrange
        CityDto cityDto = new CityDto();
        Matcher<String> matcher = Matchers.containsString("{\"name\":null,\"state\":null,\"country\":null}");
        when(restService.findCityById(anyLong())).thenReturn(cityDto);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get(format("%s/{cityId}", CITY_ENDPOINT), 12345678987654321L);
        // Act
        // Assert
        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.content().string(matcher));
    }
}
