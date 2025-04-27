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
import com.heyrudy.mybatissample.domain.model.error.CriticalRepositoryNotFoundByServiceLocatorError;
import com.heyrudy.mybatissample.domain.spi.config.AppScopedServiceLocator;
import com.heyrudy.mybatissample.domain.spi.config.CityRepositoryKey;
import com.heyrudy.mybatissample.domain.spi.config.CriticalAppScopedConfigLocatorKey;
import com.heyrudy.mybatissample.domain.spi.config.CriticalDSLContextKey;
import com.heyrudy.mybatissample.domain.spi.config.CriticalDataSourceKey;
import com.heyrudy.mybatissample.domain.spi.config.CriticalDbSecretPropertiesKey;
import com.heyrudy.mybatissample.gateway.config.AppScopedConfigLocator;
import com.heyrudy.mybatissample.gateway.config.AppScopedSecretLocator;
import com.heyrudy.mybatissample.gateway.config.DbSecretProperties;
import com.heyrudy.mybatissample.gateway.db.spring.relational.repository.CityRepository;
import io.vavr.control.Either;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.assertj.vavr.api.VavrAssertions;
import org.jooq.DSLContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CityCriticalDbAdapterTest {

    DbSecretProperties mockedDbConfigProperties;
    DataSource dataSource;
    DSLContext dslContext;
    AppScopedSecretLocator mockedAppScopedSecretLocator;
    AppScopedConfigLocator mockedAppScopedConfigLocator;
    AppScopedServiceLocator mockedAppScopedServiceLocator;
    CityRepository mockedCityRepository;

    private CityCriticalDbAdapter adapterInstanceUnderTest;

    @BeforeEach
    void setUp() {
        // Initialize all mocks in setup
        mockedDbConfigProperties = mock(DbSecretProperties.class);
        dataSource = mock(DataSource.class);
        dslContext = mock(DSLContext.class);
        mockedAppScopedSecretLocator = mock(AppScopedSecretLocator.class);
        mockedAppScopedConfigLocator = mock(AppScopedConfigLocator.class);
        mockedAppScopedServiceLocator = mock(AppScopedServiceLocator.class);
        mockedCityRepository = mock(CityRepository.class);

        adapterInstanceUnderTest = new CityCriticalDbAdapter();

        // Common mock setups that are used in multiple tests
        when(mockedAppScopedSecretLocator.getCriticalDbSecretProperties(
            CriticalDbSecretPropertiesKey.INSTANCE))
            .thenReturn(Either.right(mockedDbConfigProperties));
        when(mockedAppScopedConfigLocator.getCriticalDataSourceConfig(
            CriticalDataSourceKey.INSTANCE))
            .thenReturn(Either.right(dataSource));
        when(mockedAppScopedConfigLocator
            .getCriticalDSLContextConfig(CriticalDSLContextKey.INSTANCE))
            .thenReturn(Either.right(dslContext));
        when(mockedAppScopedServiceLocator
            .getCriticalConfig(CriticalAppScopedConfigLocatorKey.INSTANCE))
            .thenReturn(Either.right(mockedAppScopedConfigLocator));
        when(mockedAppScopedServiceLocator.getCriticalRepository(CityRepositoryKey.INSTANCE))
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
        Either<CriticalRepositoryNotFoundByServiceLocatorError, ICity> actual =
            adapterInstanceUnderTest.save(cityToSave)
                .apply(mockedAppScopedServiceLocator);

        // ASSERT - verify the result or output using assert statements
        verify(mockedAppScopedServiceLocator, times(1))
            .getCriticalRepository(CityRepositoryKey.INSTANCE);
        verify(mockedCityRepository, times(1))
            .save(isA(ICity.class));

        VavrAssertions.assertThat(actual)
            .isNotNull()
            .isRight()
            .extracting(Either::get)
            .usingRecursiveComparison()
            .isEqualTo(cityToSave);
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
        Either<CriticalRepositoryNotFoundByServiceLocatorError, List<ICity>> actual =
            adapterInstanceUnderTest.findCities()
                .apply(mockedAppScopedServiceLocator);

        // ASSERT - verify the result or output using assert statements
        verify(mockedAppScopedServiceLocator, times(1))
            .getCriticalRepository(CityRepositoryKey.INSTANCE);
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
        Either<CriticalRepositoryNotFoundByServiceLocatorError, Optional<ICity>> actual =
            adapterInstanceUnderTest.findCityById(cityId)
                .apply(mockedAppScopedServiceLocator);

        // ASSERT - verify the result or output using assert statements
        verify(mockedAppScopedServiceLocator, times(1))
            .getCriticalRepository(CityRepositoryKey.INSTANCE);
        verify(mockedCityRepository, times(1))
            .findById(cityId);

        VavrAssertions.assertThat(actual)
            .isNotNull()
            .isRight()
            .extracting(Either::get)
            .satisfies(cityOpt ->
                assertThat(cityOpt)
                    .isPresent()
                    .hasValueSatisfying(city ->
                        assertThat(city)
                            .usingRecursiveComparison()
                            .isEqualTo(expectedCity))
            );
    }
}