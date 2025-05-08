package com.heyrudy.mybatissample.domain.api;

import static org.assertj.core.api.Assertions.assertThat;

import com.heyrudy.mybatissample.domain.model.city.FullCity;
import com.heyrudy.mybatissample.domain.model.city.ICity;
import com.heyrudy.mybatissample.domain.model.error.CriticalRepositoryNotFoundByDependencyLocatorError;
import com.heyrudy.mybatissample.context.AppScopedDependencyLocator;
import com.heyrudy.mybatissample.gateway.config.TestConfig;
import io.vavr.control.Either;
import java.util.List;
import org.assertj.vavr.api.VavrAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

@Import(TestConfig.class)
class FindCitiesAPITest {

    AppScopedDependencyLocator mockedAppScopedDependencyLocator;

    private final CreateCityAPI createCityAPI = CreateCityAPI.INSTANCE;
    private final FindCitiesAPI findCitiesAPIInstanceUnderTest = FindCitiesAPI.INSTANCE;

    @BeforeEach
    void setUp() {
        // Initialize all mocks in setup
        mockedAppScopedDependencyLocator = new TestConfig.SpringTestAppScopedDependencyLocator();
    }

    @Test
    @DisplayName("fetch all cities details from database")
    void shouldFindAllCities() {
        // ARRANGE - precondition or setup
        ICity cityZero = FullCity.builder().build();
        ICity cityOne = FullCity.builder()
            .id(1L)
            .name("Paris")
            .country("France")
            .state("Paris75").build();
        createCityAPI.execute(cityZero).apply(mockedAppScopedDependencyLocator);
        createCityAPI.execute(cityOne).apply(mockedAppScopedDependencyLocator);

        // ACT - action or behavior that we are going to test
        Either<CriticalRepositoryNotFoundByDependencyLocatorError, List<ICity>> actual =
            findCitiesAPIInstanceUnderTest.execute()
                .apply(mockedAppScopedDependencyLocator);

        // ASSERT - verify the result or output using assert statements
        VavrAssertions.assertThat(actual)
            .isNotNull()
            .isRight()
            .extracting(Either::get)
            .satisfies(cities -> {
                assertThat(cities)
                    .isNotEmpty()
                    .hasSize(2);
                assertThat(cities.get(0).getId())
                    .isEqualTo(1L);
                assertThat(cities.get(1).getId())
                    .isEqualTo(2L);
                assertThat(cities.get(1).getName())
                    .isEqualTo("Paris");
            });
    }
}