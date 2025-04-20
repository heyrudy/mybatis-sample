package com.heyrudy.mybatissample.gateway.db.spring.relational;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.heyrudy.mybatissample.domain.model.city.FullCity;
import com.heyrudy.mybatissample.domain.model.city.ICity;
import com.heyrudy.mybatissample.domain.model.error.CriticalRepositoryNotFoundByLocatorError;
import com.heyrudy.mybatissample.domain.spi.config.AppScopedServiceLocator;
import com.heyrudy.mybatissample.domain.spi.config.ServiceKey.CityRepositoryKey;
import com.heyrudy.mybatissample.gateway.db.spring.relational.entity.CityEntity;
import com.heyrudy.mybatissample.gateway.db.spring.relational.repository.CityRepository;
import io.vavr.control.Either;
import java.util.List;
import java.util.Optional;
import org.assertj.vavr.api.VavrAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CityCriticalDbAdapterTest {

    private final AppScopedServiceLocator appScopedServiceLocator =
        mock(AppScopedServiceLocator.class);
    private final CityRepository mockedCityRepository =
        mock(CityRepository.class);

    private final CityCriticalDbAdapter adapterInstanceUnderTest =
        new CityCriticalDbAdapter();

    @Test
    @DisplayName("insert a new city details into database")
    void shouldInsertCity() {
        // ARRANGE - precondition or setup
        ICity expected = FullCity.builder()
            .id(1L)
            .name("Paris")
            .country("France")
            .state("Paris75").build();
        CityEntity cityEntity = CityEntity.builder()
            .id(1L)
            .name("Paris")
            .country("France")
            .state("Paris75").build();

        when(appScopedServiceLocator.getCriticalRepository(CityRepositoryKey.INSTANCE))
            .thenReturn(Either.right(mockedCityRepository));
        when(mockedCityRepository.save(isA(CityEntity.class)))
            .thenReturn(cityEntity);

        // ACT - action or behavior that we are going to test
        Either<CriticalRepositoryNotFoundByLocatorError, ICity> actual =
            adapterInstanceUnderTest.save(expected)
                .apply(appScopedServiceLocator);

        // ASSERT - verify the result or output using assert statements
        verify(mockedCityRepository, times(1))
            .save(isA(CityEntity.class));

        VavrAssertions.assertThat(actual)
            .isNotNull()
            .isRight()
            .extracting(Either::get)
            .usingRecursiveComparison()
            .isEqualTo(expected);
    }

    @Test
    @DisplayName("fetch all cities details from database")
    void shouldFindAllCities() {
        // ARRANGE - precondition or setup
        CityEntity cityEntityZero =
            CityEntity.builder().build();
        CityEntity cityEntityOne =
            CityEntity.builder()
                .id(1L)
                .name("Paris")
                .country("France")
                .state("Paris75").build();

        when(appScopedServiceLocator.getCriticalRepository(CityRepositoryKey.INSTANCE))
            .thenReturn(Either.right(mockedCityRepository));
        when(mockedCityRepository.findAll())
            .thenReturn(List.of(cityEntityZero, cityEntityOne));

        // ACT - action or behavior that we are going to test
        Either<CriticalRepositoryNotFoundByLocatorError, List<ICity>> actual =
            adapterInstanceUnderTest.findCities()
                .apply(appScopedServiceLocator);

        // ASSERT - verify the result or output using assert statements
        verify(mockedCityRepository, times(1))
            .findAll();

        VavrAssertions.assertThat(actual)
            .isNotNull()
            .isRight()
            .extracting(Either::get)
            .satisfies(fullCities -> {
                assertThat(fullCities)
                    .isNotEmpty()
                    .hasSize(2);

                assertThat(fullCities.get(0).getId())
                    .isZero();
                assertThat(fullCities.get(1).getId())
                    .isEqualTo(1L);
                assertThat(fullCities.get(1).getName())
                    .isEqualTo("Paris");
            });
    }

    @Test
    @DisplayName("fetch city details by id from database")
    void shouldFindCityById() {
        // ARRANGE - precondition or setup
        long cityId = 1L;
        ICity expected = FullCity.builder()
            .id(cityId)
            .name("Paris")
            .state("Paris75")
            .country("France").build();
        CityEntity cityEntity = CityEntity.builder()
            .id(cityId)
            .name("Paris")
            .state("Paris75")
            .country("France").build();

        when(appScopedServiceLocator.getCriticalRepository(CityRepositoryKey.INSTANCE))
            .thenReturn(Either.right(mockedCityRepository));
        when(mockedCityRepository.findById(anyLong()))
            .thenReturn(Optional.of(cityEntity));

        // ACT - action or behavior that we are going to test
        Either<CriticalRepositoryNotFoundByLocatorError, Optional<ICity>> actual =
            adapterInstanceUnderTest.findCityById(cityId)
                .apply(appScopedServiceLocator);

        // ASSERT - verify the result or output using assert statements
        verify(mockedCityRepository, times(1))
            .findById(anyLong());

        VavrAssertions.assertThat(actual)
            .isNotNull()
            .isRight()
            .extracting(Either::get)
            .satisfies(fullCityOpt ->
                assertThat(fullCityOpt)
                    .isPresent()
                    .hasValueSatisfying(it ->
                        assertThat(it)
                            .usingRecursiveComparison()
                            .isEqualTo(expected))
            );
    }
}