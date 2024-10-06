package com.heyrudy.mybatissample.gateway.db.spring.relational;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.heyrudy.mybatissample.domain.model.city.FullCity;
import com.heyrudy.mybatissample.gateway.db.spring.relational.entity.CityEntity;
import java.util.Optional;

import com.heyrudy.mybatissample.gateway.db.spring.relational.repository.CityRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CityFoundByIdGatewayTest {

    private final CityRepository mockedRepository = mock(CityRepository.class);
    private final CityFoundByIdDbAdapter cityFoundByIdGateway =
        new CityFoundByIdDbAdapter(mockedRepository);

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

        when(mockedRepository.findById(anyLong())).thenReturn(Optional.of(cityEntity));

        // ACT - action or behavior that we are going test
        Optional<FullCity> actual = cityFoundByIdGateway.findCityById(cityId);

        // ASSERT - verify the result or output using assert statements
        verify(mockedRepository, times(1)).findById(anyLong());

        assertThat(actual)
            .isPresent()
            .hasValueSatisfying(it ->
                assertThat(it)
                    .usingRecursiveComparison()
                    .isEqualTo(expected)
            );
    }
}
