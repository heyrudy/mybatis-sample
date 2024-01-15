package com.heyrudy.mybatissample.api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.heyrudy.mybatissample.domain.api.CreateCityInteractorAPI;
import com.heyrudy.mybatissample.domain.model.city.City;
import com.heyrudy.mybatissample.domain.spi.ICityDbSPI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CreateCityInteractorAPITest {

    ICityDbSPI db = mock(ICityDbSPI.class);
    private final CreateCityInteractorAPI createCityInteractorAPI = new CreateCityInteractorAPI(db);

    @Test
    @DisplayName("create city usecase")
    void testCreateCity() {
        // ARRANGE - precondition or setup
        City expected = City.builder()
            .name("Paris")
            .country("France")
            .state("Paris75")
            .build();
        when(db.save(isA(City.class))).thenReturn(expected);

        // ACT - action or behavior that we are going test
        City actual = createCityInteractorAPI.createCity(expected);

        // ASSERT - verify the result or output using assert statements
        assertThat(actual)
            .isNotNull()
            .satisfies(it ->
                assertThat(it)
                    .usingRecursiveComparison()
                    .isEqualTo(expected)
            );
    }
}
