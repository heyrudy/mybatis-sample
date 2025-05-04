package com.heyrudy.mybatissample.domain.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.heyrudy.mybatissample.domain.model.city.FullCity;
import com.heyrudy.mybatissample.domain.model.city.ICity;
import com.heyrudy.mybatissample.domain.model.error.CriticalRepositoryNotFoundByDependencyLocatorError;
import com.heyrudy.mybatissample.domain.spi.config.AppScopedDependencyLocator;
import com.heyrudy.mybatissample.domain.spi.config.CityRepositoryKey;
import com.heyrudy.mybatissample.gateway.db.spring.relational.repository.CityRepository;
import io.vavr.control.Either;
import java.util.List;
import org.assertj.vavr.api.VavrAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FindCitiesAPITest {

    AppScopedDependencyLocator mockedAppScopedDependencyLocator;
    CityRepository mockedCityRepository;

    private final FindCitiesAPI findCitiesAPIInstanceUnderTest = FindCitiesAPI.INSTANCE;

    @BeforeEach
    void setUp() {
        // Initialize all mocks in setup
        mockedAppScopedDependencyLocator = mock(AppScopedDependencyLocator.class);
        mockedCityRepository = mock(CityRepository.class);

        // Common mock setups that are used in multiple tests
        when(mockedAppScopedDependencyLocator.getDependency(CityRepositoryKey.INSTANCE))
            .thenReturn(Either.right(mockedCityRepository));
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

        when(mockedCityRepository.findAll())
            .thenReturn(r -> Either.right(List.of(cityZero, cityOne)));

        // ACT - action or behavior that we are going to test
        Either<CriticalRepositoryNotFoundByDependencyLocatorError, List<ICity>> actual =
            findCitiesAPIInstanceUnderTest.execute()
                .apply(mockedAppScopedDependencyLocator);

        // ASSERT - verify the result or output using assert statements
        verify(mockedAppScopedDependencyLocator, times(1))
            .getDependency(CityRepositoryKey.INSTANCE);
        verify(mockedCityRepository, times(1))
            .findAll();

        VavrAssertions.assertThat(actual)
            .isNotNull()
            .isRight()
            .extracting(Either::get)
            .satisfies(cities -> {
                assertThat(cities)
                    .isNotEmpty()
                    .hasSize(2);
                assertThat(cities.get(0).getId())
                    .isZero();
                assertThat(cities.get(1).getId())
                    .isEqualTo(1L);
                assertThat(cities.get(1).getName())
                    .isEqualTo("Paris");
            });
    }
}