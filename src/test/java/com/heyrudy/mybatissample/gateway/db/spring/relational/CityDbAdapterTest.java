package com.heyrudy.mybatissample.gateway.db.spring.relational;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.heyrudy.mybatissample.domain.model.city.FullCity;
import com.heyrudy.mybatissample.gateway.db.spring.relational.entity.CityEntity;
import com.heyrudy.mybatissample.gateway.db.spring.relational.repository.CityRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CityDbAdapterTest {

    private final CityRepository mockedCityRepository = mock(CityRepository.class);
    private final CityDbAdapter cityGateway =
        new CityDbAdapter(mockedCityRepository);

    @Test
    @DisplayName("insert a new city details into database")
    void shouldInsertCity() {
        // ARRANGE - precondition or setup
        FullCity expected = FullCity.builder()
            .id(1L)
            .name("Paris")
            .country("France")
            .state("Paris75")
            .build();
        CityEntity cityEntity = CityEntity.builder()
            .id(1L)
            .name("Paris")
            .country("France")
            .state("Paris75")
            .build();

        when(mockedCityRepository.save(isA(CityEntity.class))).thenReturn(cityEntity);

        // ACT - action or behavior that we are going test
        FullCity actual = cityGateway.save(expected);

        // ASSERT - verify the result or output using assert statements
        verify(mockedCityRepository, times(1)).save(isA(CityEntity.class));

        assertThat(actual)
            .usingRecursiveComparison()
            .isEqualTo(expected);
    }

    @Test
    @DisplayName("fetch all cities details from database")
    void shouldFindAllCities() {
        // ARRANGE - precondition or setup
        CityEntity cityEntityZero = CityEntity.builder().build();
        CityEntity cityEntityOne = CityEntity.builder()
            .id(1L)
            .name("Paris")
            .country("France")
            .state("Paris75")
            .build();

        when(mockedCityRepository.findAll()).thenReturn(List.of(cityEntityZero, cityEntityOne));

        // ACT - action or behavior that we are going test
        List<FullCity> actual = cityGateway.findCities().stream().toList();

        // ASSERT - verify the result or output using assert statements
        verify(mockedCityRepository, times(1)).findAll();

        assertThat(actual)
            .isNotNull()
            .isNotEmpty()
            .hasSize(2);
        assertThat(actual.get(0).getId())
            .isZero();
        assertThat(actual.get(1).getId())
            .isEqualTo(1L);
        assertThat(actual.get(1).getName())
            .isEqualTo("Paris");
    }

    @Test
    @DisplayName("fetch city details by id from database")
    void shouldFindCityById() {
        // ARRANGE - precondition or setup
        long cityId = 1L;
        FullCity expected = FullCity.builder()
            .id(cityId)
            .name("Paris")
            .state("Paris75")
            .country("France")
            .build();
        CityEntity cityEntity = CityEntity.builder()
            .id(cityId)
            .name("Paris")
            .state("Paris75")
            .country("France")
            .build();

        when(mockedCityRepository.findById(anyLong())).thenReturn(Optional.of(cityEntity));

        // ACT - action or behavior that we are going test
        Optional<FullCity> actual = cityGateway.findCityById(cityId);

        // ASSERT - verify the result or output using assert statements
        verify(mockedCityRepository, times(1)).findById(anyLong());

        assertThat(actual)
            .isPresent()
            .hasValueSatisfying(it ->
                assertThat(it)
                    .usingRecursiveComparison()
                    .isEqualTo(expected)
            );
    }
}