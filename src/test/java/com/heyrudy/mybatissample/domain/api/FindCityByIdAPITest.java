package com.heyrudy.mybatissample.domain.api;

import static org.assertj.core.api.Assertions.assertThat;

import com.heyrudy.mybatissample.domain.model.city.FullCity;
import com.heyrudy.mybatissample.domain.model.city.ICity;
import com.heyrudy.mybatissample.domain.model.common.CityCriteriaDetails;
import com.heyrudy.mybatissample.domain.model.error.CityNotFoundError;
import com.heyrudy.mybatissample.context.AppScopedDependencyLocator;
import com.heyrudy.mybatissample.gateway.config.TestConfig;
import io.vavr.control.Either;
import org.assertj.vavr.api.VavrAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

@Import(TestConfig.class)
class FindCityByIdAPITest {

    AppScopedDependencyLocator mockedAppScopedDependencyLocator;

    private final CreateCityAPI createCityAPI = CreateCityAPI.INSTANCE;
    private final FindCityByIdAPI findCityByIdAPIInstanceUnderTest = FindCityByIdAPI.INSTANCE;

    @BeforeEach
    void setUp() {
        // Initialize all mocks in setup
        mockedAppScopedDependencyLocator = new TestConfig.SpringTestAppScopedDependencyLocator();
    }

    @Test
    @DisplayName("fetch city details by id from database")
    void shouldFindCityById() {
        // ARRANGE - precondition or setup
        long cityId = 1L;
        ICity expectedCity = FullCity.builder()
            .name("Paris")
            .state("Paris75")
            .country("France").build();
        createCityAPI.execute(expectedCity).apply(mockedAppScopedDependencyLocator);

        // ACT - action or behavior that we are going to test
        Either<CityNotFoundError, ICity> actual =
            findCityByIdAPIInstanceUnderTest.execute(
                    CityCriteriaDetails.builder().cityId(cityId).build())
                .apply(mockedAppScopedDependencyLocator);

        // ASSERT - verify the result or output using assert statements
        VavrAssertions.assertThat(actual)
            .isNotNull()
            .isRight()
            .extracting(Either::get)
            .satisfies(iCity ->
                assertThat(iCity)
                    .usingRecursiveComparison()
                    .isEqualTo(expectedCity)
            );
    }
}