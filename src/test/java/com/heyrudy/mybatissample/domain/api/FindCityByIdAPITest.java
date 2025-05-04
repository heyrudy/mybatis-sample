package com.heyrudy.mybatissample.domain.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.heyrudy.mybatissample.domain.model.city.FullCity;
import com.heyrudy.mybatissample.domain.model.city.ICity;
import com.heyrudy.mybatissample.domain.model.common.CityCriteriaDetails;
import com.heyrudy.mybatissample.domain.model.error.CityNotFoundError;
import com.heyrudy.mybatissample.domain.spi.config.AppScopedDependencyLocator;
import com.heyrudy.mybatissample.domain.spi.config.CityRepositoryKey;
import com.heyrudy.mybatissample.gateway.db.spring.relational.repository.CityRepository;
import io.vavr.control.Either;
import java.util.Optional;
import org.assertj.vavr.api.VavrAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FindCityByIdAPITest {

    AppScopedDependencyLocator mockedAppScopedDependencyLocator;
    CityRepository mockedCityRepository;

    private final FindCityByIdAPI findCityByIdAPIInstanceUnderTest = FindCityByIdAPI.INSTANCE;

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
    @DisplayName("fetch city details by id from database")
    void shouldFindCityById() {
        // ARRANGE - precondition or setup
        long cityId = 1L;
        ICity expectedCity = FullCity.builder()
            .id(cityId)
            .name("Paris")
            .state("Paris75")
            .country("France").build();

        when(mockedCityRepository.findById(anyLong()))
            .thenReturn(r -> Either.right(Optional.of(expectedCity)));

        // ACT - action or behavior that we are going to test
        Either<CityNotFoundError, ICity> actual =
            findCityByIdAPIInstanceUnderTest.execute(
                    CityCriteriaDetails.builder().cityId(cityId).build())
                .apply(mockedAppScopedDependencyLocator);

        // ASSERT - verify the result or output using assert statements
        verify(mockedAppScopedDependencyLocator, times(1))
            .getDependency(CityRepositoryKey.INSTANCE);
        verify(mockedCityRepository, times(1))
            .findById(anyLong());

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