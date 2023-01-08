package com.heyrudy.mybatissample.app.api.internal.handlers.dbstorage.spring;

import com.heyrudy.mybatissample.app.api.internal.handlers.dbstorage.spring.entity.City;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends CrudRepository<City, Long> {

    @Modifying
    @Query("UPDATE City c SET c.state = :state WHERE c.id = :id")
    void updateCityById(@Param("id") final long id, @Param("state") final String state);
}
