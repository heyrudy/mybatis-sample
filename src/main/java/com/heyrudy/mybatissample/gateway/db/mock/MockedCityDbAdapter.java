package com.heyrudy.mybatissample.gateway.db.mock;

import com.heyrudy.mybatissample.domain.model.city.FullCity;
import com.heyrudy.mybatissample.domain.spi.ICityDbSPI;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class MockedCityDbAdapter implements ICityDbSPI {

    private final static Map<Long, FullCity> IN_MEMORY_DB = new ConcurrentHashMap<>();

    @Override
    public FullCity save(FullCity fullCity) {
        IN_MEMORY_DB.put(fullCity.getId(), fullCity);
        return IN_MEMORY_DB.get(fullCity.getId());
    }

    @Override
    public List<FullCity> findCities() {
        return IN_MEMORY_DB.values().stream().toList();
    }

    @Override
    public Optional<FullCity> findCityById(long id) {
        return Optional.ofNullable(IN_MEMORY_DB.get(id));
    }
}
