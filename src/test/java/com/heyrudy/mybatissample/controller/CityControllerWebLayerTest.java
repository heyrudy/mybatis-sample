package com.heyrudy.mybatissample.controller;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.API.Match;
import static java.lang.String.format;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heyrudy.mybatissample.controller.rest.CityController;
import com.heyrudy.mybatissample.controller.rest.dto.CityRequestDTO;
import com.heyrudy.mybatissample.domain.model.error.DBServiceNotFoundByLocatorError;
import com.heyrudy.mybatissample.domain.spi.AppScopedLocator;
import com.heyrudy.mybatissample.domain.spi.ServiceKey;
import com.heyrudy.mybatissample.domain.spi.ServiceKey.CityDbKey;
import com.heyrudy.mybatissample.domain.spi.ServiceKey.DbServiceKey;
import com.heyrudy.mybatissample.gateway.db.mock.MockedCityDbAdapter;
import io.vavr.control.Either;
import io.vavr.control.Option;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
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

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("create city endpoint")
    void createCity() throws Exception {
        // ARRANGE - precondition or setup
        CityRequestDTO cityRequestDTO =
            CityRequestDTO.builder()
                .name("Paris")
                .country("France")
                .state("Paris75").build();

        MockHttpServletRequestBuilder requestBuilder =
            MockMvcRequestBuilders.post(CITIES_API_V_1_ENDPOINT);

        // ACT - action or behavior that we are going test
        ResultActions actualPerformResult = mockMvc.perform(
            requestBuilder.contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cityRequestDTO))
        );

        // ASSERT - verify the result or output using assert statements
        actualPerformResult.andExpect(status().isCreated());
    }

    @Test
    @DisplayName("find city by its id first use-case")
    void findCities() throws Exception {
        // ARRANGE - precondition or setup
        Matcher<String> matcher = Matchers.containsString("[]");

        // ACT - action or behavior that we are going test
        MockHttpServletRequestBuilder requestBuilder =
            MockMvcRequestBuilders.get(CITIES_API_V_1_ENDPOINT);

        // ASSERT - verify the result or output using assert statements
        mockMvc.perform(requestBuilder)
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().string(matcher));
    }

    @Test
    @DisplayName("find a city by its id second use-case")
    void findCityById() throws Exception {
        // ARRANGE - precondition or setup
        Matcher<String> matcher = Matchers.containsString("not found");

        // ACT - action or behavior that we are going test
        MockHttpServletRequestBuilder requestBuilder =
            MockMvcRequestBuilders.get(
                format("%s/{cityId}", CITIES_API_V_1_ENDPOINT), 1L);

        // ASSERT - verify the result or output using assert statements
        mockMvc.perform(requestBuilder)
            .andExpect(status().isBadRequest())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.content().string(matcher));
    }

    @TestConfiguration
    public static class MockedTestConfig {

        @Bean
        @Primary
        public AppScopedLocator mockedLocator() {
            return new MockedTestAppScopedLocator();
        }

        public static class MockedTestAppScopedLocator implements AppScopedLocator {

            @Override
            public <T> Either<DBServiceNotFoundByLocatorError, T> getDBService(
                DbServiceKey<T> key) {
                return getService(key)
                    .toEither(new DBServiceNotFoundByLocatorError(
                        "No DB service found for key: %s".formatted(key)));
            }

            @Override
            public <T> Option<T> getService(ServiceKey<T> key) {
                return Option.of(serviceMap().get(key))
                    .filter(key.getType()::isInstance)
                    .flatMap(it ->
                        Match(it).option(
                            Case($(key.getType()::isInstance), key.getType()::cast)));
            }

            @Override
            public boolean hasService(ServiceKey<?> key) {
                return serviceMap().containsKey(key);
            }

            private Map<ServiceKey<?>, ?> serviceMap() {
                return Stream.of(cityServiceMap())
                    .flatMap(it -> it.entrySet().stream())
                    .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
            }

            private Map<ServiceKey<?>, ?> cityServiceMap() {
                return Map.ofEntries(
                    Map.entry(
                        CityDbKey.INSTANCE, new MockedCityDbAdapter())
                );
            }
        }
    }
}
