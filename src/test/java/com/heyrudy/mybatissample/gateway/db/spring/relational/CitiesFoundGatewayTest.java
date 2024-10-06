package com.heyrudy.mybatissample.gateway.db.spring.relational;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.heyrudy.mybatissample.domain.model.city.FullCity;
import com.heyrudy.mybatissample.gateway.db.spring.relational.entity.CityEntity;
import java.util.List;

import com.heyrudy.mybatissample.gateway.db.spring.relational.repository.CityRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CitiesFoundGatewayTest {

    private final CityRepository mockedRepository = mock(CityRepository.class);
    private final CitiesFoundDbAdapter citiesFoundGateway =
        new CitiesFoundDbAdapter(mockedRepository);

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

        when(mockedRepository.findAll()).thenReturn(List.of(cityEntityZero, cityEntityOne));

        // ACT - action or behavior that we are going test
        List<FullCity> actual = citiesFoundGateway.findCities().stream().toList();

        // ASSERT - verify the result or output using assert statements
        verify(mockedRepository, times(1)).findAll();

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
}
