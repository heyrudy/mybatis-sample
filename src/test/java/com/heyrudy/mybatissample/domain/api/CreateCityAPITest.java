package com.heyrudy.mybatissample.domain.api;

import static org.mockito.ArgumentMatchers.isA;
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
import org.assertj.vavr.api.VavrAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CreateCityAPITest {

    AppScopedDependencyLocator mockedAppScopedDependencyLocator;
    CityRepository mockedCityRepository;

    private final CreateCityAPI createCityAPIInstanceUnderTest = CreateCityAPI.INSTANCE;

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
    @DisplayName("insert a new city details into database")
    void shouldInsertCity() {
        // ARRANGE - precondition or setup
        ICity cityToSave = FullCity.builder()
            .id(1L)
            .name("Paris")
            .country("France")
            .state("Paris75").build();

        when(mockedCityRepository.save(isA(ICity.class)))
            .thenReturn(r -> Either.right(cityToSave));

        // ACT - action or behavior that we are going to test
        Either<CriticalRepositoryNotFoundByDependencyLocatorError, ICity> actual =
            createCityAPIInstanceUnderTest.execute(cityToSave)
                .apply(mockedAppScopedDependencyLocator);

        // ASSERT - verify the result or output using assert statements
        verify(mockedAppScopedDependencyLocator, times(1))
            .getDependency(CityRepositoryKey.INSTANCE);
        verify(mockedCityRepository, times(1))
            .save(isA(ICity.class));

        VavrAssertions.assertThat(actual)
            .isNotNull()
            .isRight()
            .extracting(Either::get)
            .usingRecursiveComparison()
            .isEqualTo(cityToSave);
    }
}