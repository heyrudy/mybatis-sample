package com.heyrudy.mybatissample.gateway.store.spring.relational;

import com.heyrudy.mybatissample.domain.model.city.City;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CityEntityRepositoryDBLayerTest {

    private final CityDbAdapter repository = mock(CityDbAdapter.class);

    @Test
    @DisplayName("insert a new city details into database")
    void shouldInsertCity() {
        // ARRANGE - precondition or setup
        City expected = City.builder()
            .id(1L)
            .name("Paris")
            .country("France")
            .state("Paris75")
            .build();
        when(repository.save(expected)).thenReturn(expected);

        // ACT - action or behavior that we are going test
        City actual = repository.save(expected);

        // ASSERT - verify the result or output using assert statements
        assertThat(actual)
            .usingRecursiveComparison()
            .isEqualTo(expected);
    }

    @Test
    @DisplayName("fetch city details by id from database")
    void shouldFindCityById() {
        // ARRANGE - precondition or setup
        long cityId = 1L;
        City city = City.builder()
            .id(cityId)
            .name("Paris")
            .state("Paris75")
            .country("France")
            .build();
        when(repository.findCityById(anyLong())).thenReturn(Optional.of(city));

        // ACT - action or behavior that we are going test
        Optional<City> actual = repository.findCityById(cityId);

        // ASSERT - verify the result or output using assert statements
        assertThat(actual)
            .isPresent()
            .hasValueSatisfying(it ->
                assertThat(it)
                    .usingRecursiveComparison()
                    .isEqualTo(city)
            );
    }

    @Test
    @DisplayName("fetch all cities details from database")
    void shouldFindAllCities() {
        // ARRANGE - precondition or setup
        City cityZero = City.builder().build();
        City cityOne = City.builder()
            .id(1L)
            .name("Paris")
            .country("France")
            .state("Paris75")
            .build();
        when(repository.findCities()).thenReturn(List.of(cityZero, cityOne));

        // ACT - action or behavior that we are going test
        List<City> actual = repository.findCities().stream().toList();

        // ASSERT - verify the result or output using assert statements
        assertThat(actual)
            .hasSize(2);
        assertThat(actual.get(0).getId())
            .isZero();
        assertThat(actual.get(1).getId())
            .isEqualTo(1L);
        assertThat(actual.get(1).getName())
            .isEqualTo("Paris");
    }
}
