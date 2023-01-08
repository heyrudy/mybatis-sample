package com.heyrudy.mybatissample.app.api.internal.handlers.dbstorage.spring;

import com.heyrudy.mybatissample.app.api.internal.handlers.dbstorage.spring.entity.City;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CityRepositoryUnitTest {

    @Mock
    private CityRepository repository;

    @Test
    @DisplayName("insert a new city details into database")
    void shouldInsertCity() {
        // Arrange
        long cityId = 1L;
        City expected = City.builder()
                .id(cityId)
                .name("Paris")
                .country("France")
                .state("Paris75")
                .build();
        doNothing().when(repository).save(expected);
        // Act
        repository.save(expected);
        // Assert
        when(repository.findById(cityId)).thenReturn(Optional.of(expected));
        assertThat(repository.findById(cityId))
                .isPresent()
                .satisfies(actual -> {
                    assertThat(actual)
                            .map(City::getId)
                            .hasValue(expected.getId());
                    assertThat(actual)
                            .map(City::getName)
                            .hasValue(expected.getName());
                    assertThat(actual)
                            .map(City::getState)
                            .hasValue(expected.getState());
                    assertThat(actual)
                            .map(City::getCountry)
                            .hasValue(expected.getCountry());
                });
    }

    @Test
    @DisplayName("fetch city details by id from database")
    void shouldFindCityById() {
        // Arrange
        long cityId = 1L;
        City city = City.builder()
                .id(1L)
                .name("Paris")
                .country("France")
                .state("Paris75")
                .build();
        when(repository.findById(cityId)).thenReturn(Optional.of(city));
        // Act
        // Assert
        assertThat(repository.findById(cityId))
                .isPresent()
                .hasValueSatisfying(cityFound -> {
                        assertThat(cityFound)
                                .extracting(City::getId)
                                .isEqualTo(1L);
                        assertThat(cityFound)
                                .extracting(City::getName)
                                .isEqualTo("Paris");
                        assertThat(cityFound)
                                .extracting(City::getState)
                                .isEqualTo("Paris75");
                        assertThat(cityFound)
                                .extracting(City::getCountry)
                                .isEqualTo("France");
                });
    }

    @Test
    @DisplayName("fetch all cities details from database")
    void shouldFindAllCities() {
        // Arrange
        City city = new City();
        City cityOne = City.builder()
                .id(1L)
                .name("Paris")
                .country("France")
                .state("Paris75")
                .build();
        ArrayList<City> cities = new ArrayList<>();
        cities.add(city);
        cities.add(cityOne);
        when(repository.findAll()).thenReturn(cities);
        // Act
        List<City> actual = StreamSupport.stream(repository.findAll().spliterator(), false).toList();
        // Assert
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
