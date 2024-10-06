package com.heyrudy.mybatissample.gateway.db.spring.relational;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.heyrudy.mybatissample.domain.model.city.FullCity;
import com.heyrudy.mybatissample.gateway.db.spring.relational.entity.CityEntity;
import com.heyrudy.mybatissample.gateway.db.spring.relational.repository.CityRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CityRegisterGatewayTest {

    private final CityRepository mockedRepository = mock(CityRepository.class);
    private final CityRegisterDbAdapter cityRegisterGateway =
        new CityRegisterDbAdapter(mockedRepository);

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

        when(mockedRepository.save(isA(CityEntity.class))).thenReturn(cityEntity);

        // ACT - action or behavior that we are going test
        FullCity actual = cityRegisterGateway.save(expected);

        // ASSERT - verify the result or output using assert statements
        verify(mockedRepository, times(1)).save(isA(CityEntity.class));

        assertThat(actual)
            .usingRecursiveComparison()
            .isEqualTo(expected);
    }
}
