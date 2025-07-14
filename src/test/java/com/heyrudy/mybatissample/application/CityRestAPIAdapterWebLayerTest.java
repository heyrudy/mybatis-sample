package com.heyrudy.mybatissample.application;

//import static io.vavr.API.$;
//import static io.vavr.API.Case;
//import static io.vavr.API.Match;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//import com.heyrudy.mybatissample.config.AppScopedLocator;
//import com.heyrudy.mybatissample.config.ServiceKey;
//import com.heyrudy.mybatissample.config.ServiceKey.CityDbSPIKey;
//import com.heyrudy.mybatissample.config.ServiceKey.CityRestAPIKey;
//import com.heyrudy.mybatissample.config.ServiceKey.DbSPICriticalServiceKey;
//import com.heyrudy.mybatissample.config.ServiceKey.RestAPICriticalServiceKey;
//import com.heyrudy.mybatissample.controller.rest.CityRestAPIAdapter;
//import com.heyrudy.mybatissample.controller.rest.dto.CityRequestDTO;
//import com.heyrudy.mybatissample.controller.rest.mock.MockedCityRestAPIAdapter;
//import com.heyrudy.mybatissample.domain.model.error.DbSPICriticalServiceNotFoundByLocatorError;
//import com.heyrudy.mybatissample.domain.model.error.RestAPICriticalServiceNotFoundByLocatorError;
//import com.heyrudy.mybatissample.gateway.db.mock.MockedCityDbSPIAdapter;
//import io.vavr.control.Either;
//import io.vavr.control.Option;
//import java.util.Map;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Primary;
//import org.springframework.web.servlet.function.EntityResponse;
//import org.springframework.web.servlet.function.ServerRequest;
//import org.springframework.web.servlet.function.ServerResponse;

class CityRestAPIAdapterWebLayerTest {

//    private final ServerRequest request = mock(ServerRequest.class);
//
//    private final CityRestAPIAdapter cityRestAPIAdapter = mock(CityRestAPIAdapter.class);
//
//    @Test
//    @DisplayName("create city endpoint")
//    void createCity() throws Exception {
//        // ARRANGE - precondition or setup
//        CityRequestDTO cityRequestDTO =
//            CityRequestDTO.builder()
//                .name("Paris")
//                .country("France")
//                .state("Paris75").build();
//
//        when(request.body(CityRequestDTO.class)).thenReturn(cityRequestDTO);
//
//        // ACT - action or behavior that we are going test
//        ServerResponse response = cityRestAPIAdapter.createCity(request);
//
//        // ASSERT - verify the result or output using assert statements
//        assertThat(response instanceof EntityResponse)
//            .isTrue();
//        assertThat(response.statusCode().value())
//            .isEqualTo(201);
//    }
//
//    @Test
//    @DisplayName("find cities first use-case")
//    void findCities() {
//        // ARRANGE - precondition or setup
//
//        // ACT - action or behavior that we are going test
//        ServerResponse response = cityRestAPIAdapter.findCities();
//
//        // ASSERT - verify the result or output using assert statements
//        assertThat(response instanceof EntityResponse)
//            .isTrue();
//        assertThat(response.statusCode().value())
//            .isEqualTo(200);
//    }
//
//    @Test
//    @DisplayName("find a city by its id second use-case")
//    void findCityById() {
//        // ARRANGE - precondition or setup
//        Map<String, String> pathVariables = Map.ofEntries(
//            Map.entry("id", "1")
//        );
//        ServerResponse mockResponse = EntityResponse.fromObject("city").status(200).build();
//
//        when(request.pathVariables()).thenReturn(pathVariables);
//        when(cityRestAPIAdapter.findCityById(request)).thenReturn(mockResponse);
//
//        // ACT - action or behavior that we are going test
//        ServerResponse response = cityRestAPIAdapter.findCityById(request);
//
//        // ASSERT - verify the result or output using assert statements
//        assertThat(response instanceof EntityResponse)
//            .isTrue();
//        assertThat(response.statusCode().value())
//            .isEqualTo(200);
//    }
//
//    @TestConfiguration
//    public static class MockedTestConfig {
//
//        @Bean
//        @Primary
//        public AppScopedLocator mockedLocator() {
//            return new MockedTestAppScopedLocator();
//        }
//
//        public static class MockedTestAppScopedLocator implements AppScopedLocator {
//
//            @Override
//            public <T> Either<RestAPICriticalServiceNotFoundByLocatorError, T> getRestAPICriticalService(
//                RestAPICriticalServiceKey<T> key) {
//                return getService(key)
//                    .toEither(new RestAPICriticalServiceNotFoundByLocatorError(
//                        "No Rest API service found for key: %s".formatted(key)));
//            }
//
//            @Override
//            public <T> Either<DbSPICriticalServiceNotFoundByLocatorError, T> getDbSPICriticalService(
//                DbSPICriticalServiceKey<T> key) {
//                return getService(key)
//                    .toEither(new DbSPICriticalServiceNotFoundByLocatorError(
//                        "No DB SPI service found for key: %s".formatted(key)));
//            }
//
//            @Override
//            public <T> Option<T> getService(ServiceKey<T> key) {
//                return Option.of(serviceMap().get(key))
//                    .filter(key.getType()::isInstance)
//                    .flatMap(it ->
//                        Match(it).option(
//                            Case($(key.getType()::isInstance), key.getType()::cast)));
//            }
//
//            @Override
//            public boolean hasService(ServiceKey<?> key) {
//                return serviceMap().containsKey(key);
//            }
//
//            private Map<ServiceKey<?>, ?> serviceMap() {
//                return Stream.of(cityServiceMap())
//                    .flatMap(it -> it.entrySet().stream())
//                    .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
//            }
//
//            private Map<ServiceKey<?>, ?> cityServiceMap() {
//                return Map.ofEntries(
//                    Map.entry(
//                        CityRestAPIKey.INSTANCE, new MockedCityRestAPIAdapter()),
//                    Map.entry(
//                        CityDbSPIKey.INSTANCE, new MockedCityDbSPIAdapter())
//                );
//            }
//        }
//    }
}
