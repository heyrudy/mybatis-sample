package com.heyrudy.mybatissample.repositories;

import com.heyrudy.mybatissample.domain.City;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {CityRepository.class})
@ExtendWith(SpringExtension.class)
class CityRepositoryTest {

    @MockBean
    private CityRepository repository;

    @Test
    @DisplayName("insert a new city details into database")
    void shouldInsertCity() {
        // Arrange
        long cityId = 1L;
        City expected = City.builder()
                .cityId(cityId)
                .name("Paris")
                .country("France")
                .state("Paris75")
                .build();
        doNothing().when(repository).insertCity(expected);
        // Act
        repository.insertCity(expected);
        // Assert
        Optional<City> actualOptional = repository.findCityById(cityId);
        actualOptional.ifPresent(actual -> {
            assertEquals(expected.getCityId(), actual.getCityId());
            assertEquals(expected.getName(), actual.getName());
            assertEquals(expected.getState(), actual.getState());
            assertEquals(expected.getCountry(), actual.getCountry());
        });
    }

    @Test
    @DisplayName("fetch city details by id from database")
    void shouldFindCityById() {
        // Arrange
        long cityId = 1L;
        City city = City.builder()
                .cityId(1L)
                .name("Paris")
                .country("France")
                .state("Paris75")
                .build();
        when(repository.findCityById(cityId)).thenReturn(Optional.of(city));
        // Act
        // Assert
        assertAll("properties",
                () -> assertEquals(1L, city.getCityId()),
                () -> assertEquals("Paris", city.getName()),
                () -> assertEquals("Paris75", city.getState()),
                () -> assertEquals("France", city.getCountry())
        );
    }

    @Test
    @DisplayName("fetch all cities details from database")
    void shouldFindAllCities() {
        // Arrange
        City city = new City();
        City cityOne = City.builder()
                .cityId(1L)
                .name("Paris")
                .country("France")
                .state("Paris75")
                .build();
        ArrayList<City> cities = new ArrayList<>();
        cities.add(city);
        cities.add(cityOne);
        when(repository.findAllCities()).thenReturn(cities);
        // Act
        List<City> actual = repository.findAllCities();
        // Assert
        assertEquals(2, actual.size());
        assertEquals(0L, actual.get(0).getCityId());
        assertEquals(1L, actual.get(1).getCityId());
        assertEquals("Paris", actual.get(1).getName());
    }
}
