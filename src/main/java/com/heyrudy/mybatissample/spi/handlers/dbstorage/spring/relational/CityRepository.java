package com.heyrudy.mybatissample.spi.handlers.dbstorage.spring.relational;

import com.heyrudy.mybatissample.core.model.City;
import com.heyrudy.mybatissample.core.spi.dbstorage.ICityStore;
import com.heyrudy.mybatissample.spi.handlers.dbstorage.spring.relational.entity.CityEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Repository
public interface CityRepository extends CrudRepository<CityEntity, Long>, ICityStore {

    @Modifying
    @Query("UPDATE CityEntity c SET c.state = :state WHERE c.id = :id")
    void updateCityById(@Param("id") final long id, @Param("state") final String state);

    @Override
    default City save(City city) {
        CityEntity cityEntitySaved = save(
                CityEntity.builder()
                        .id(city.getId())
                        .name(city.getName())
                        .state(city.getState())
                        .country(city.getCountry())
                        .build()
        );
        return City.builder()
                .id(cityEntitySaved.getId())
                .name(cityEntitySaved.getName())
                .state(cityEntitySaved.getState())
                .country(cityEntitySaved.getCountry())
                .build();
    }

    @Override
    default Optional<City> findCityById(long id) {
        return findById(id)
                .map(it ->
                        City.builder()
                                .id(it.getId())
                                .name(it.getName())
                                .state(it.getState())
                                .country(it.getCountry())
                                .build()
                );
    }


    @Override
    default List<City> findCities() {
        return StreamSupport
                .stream(findAll().spliterator(), false)
                .map(it ->
                        City.builder()
                                .id(it.getId())
                                .name(it.getName())
                                .state(it.getState())
                                .country(it.getCountry())
                                .build()
                ).toList();
    }
}
