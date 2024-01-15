package com.heyrudy.mybatissample.api.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.heyrudy.mybatissample.domain.api.FindCitiesInteractorAPI;
import com.heyrudy.mybatissample.domain.model.city.City;
import com.heyrudy.mybatissample.domain.spi.ICityDbSPI;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FindCitiesInteractorAPITest {

    ICityDbSPI db = mock(ICityDbSPI.class);
    private final FindCitiesInteractorAPI findCitiesInteractorAPI = new FindCitiesInteractorAPI(db);

    @Test
    @DisplayName("find all cities usecase (1)")
    void testFindCities() {
        // ARRANGE - precondition or setup
        when(db.findCities()).thenReturn(Collections.emptyList());

        // ACT - action or behavior that we are going test
        List<City> actual = findCitiesInteractorAPI.findCities();

        // ASSERT - verify the result or output using assert statements
        assertThat(actual)
            .isNotNull()
            .isEmpty();
    }

    @Test
    @DisplayName("find all cities usecase (2)")
    void testFindCities2() {
        // ARRANGE - precondition or setup
        when(db.findCities()).thenReturn(List.of(City.builder().build()));

        // ACT - action or behavior that we are going test
        List<City> actual = findCitiesInteractorAPI.findCities();

        // ASSERT - verify the result or output using assert statements
        assertThat(actual)
            .isNotNull()
            .isNotEmpty()
            .hasSize(1);
    }
}
