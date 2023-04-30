package com.heyrudy.mybatissample.app.api.internal.handlers.dbstorage.spring;

import com.heyrudy.mybatissample.api.handlers.dbstorage.spring.CityRepository;
import com.heyrudy.mybatissample.api.handlers.dbstorage.spring.entity.CityEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CityEntityRepositoryDBLayerTest {

    private final CityRepository repository = mock(CityRepository.class);

    @Test
    @DisplayName("insert a new city details into database")
    void shouldInsertCity() {
        // ARRANGE - precondition or setup
        long cityId = 1L;
        CityEntity expected = CityEntity.builder()
                .id(cityId)
                .name("Paris")
                .country("France")
                .state("Paris75")
                .build();
        when(repository.save(expected)).thenReturn(expected);

        // ACT - action or behaviour that we are going test
        repository.save(expected);

        // ASSERT - verify the result or output using assert statements
        when(repository.findById(cityId)).thenReturn(Optional.of(expected));
        assertThat(repository.findById(cityId))
                .isPresent()
                .satisfies(actual -> {
                    assertThat(actual)
                            .map(CityEntity::getId)
                            .hasValue(expected.getId());
                    assertThat(actual)
                            .map(CityEntity::getName)
                            .hasValue(expected.getName());
                    assertThat(actual)
                            .map(CityEntity::getState)
                            .hasValue(expected.getState());
                    assertThat(actual)
                            .map(CityEntity::getCountry)
                            .hasValue(expected.getCountry());
                });
    }

    @Test
    @DisplayName("fetch city details by id from database")
    void shouldFindCityById() {
        // ARRANGE - precondition or setup
        long cityId = 1L;
        CityEntity cityEntity = CityEntity.builder()
                .id(1L)
                .name("Paris")
                .country("France")
                .state("Paris75")
                .build();
        when(repository.findById(cityId)).thenReturn(Optional.of(cityEntity));

        // ACT - action or behaviour that we are going test

        // ASSERT - verify the result or output using assert statements
        assertThat(repository.findById(cityId))
                .isPresent()
                .hasValueSatisfying(cityFound -> {
                    assertThat(cityFound)
                            .extracting(CityEntity::getId)
                            .isEqualTo(1L);
                    assertThat(cityFound)
                            .extracting(CityEntity::getName)
                            .isEqualTo("Paris");
                    assertThat(cityFound)
                            .extracting(CityEntity::getState)
                            .isEqualTo("Paris75");
                    assertThat(cityFound)
                            .extracting(CityEntity::getCountry)
                            .isEqualTo("France");
                });
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
        when(repository.findAll()).thenReturn(List.of(cityEntityZero, cityEntityOne));

        // ACT - action or behaviour that we are going test
        List<CityEntity> actual = StreamSupport.stream(repository.findAll().spliterator(), false).toList();

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
