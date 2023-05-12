package com.heyrudy.mybatissample.gateway.store.spring.relational;

import com.heyrudy.mybatissample.gateway.store.spring.relational.entity.CityEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CityRepository extends CrudRepository<CityEntity, Long> {

    @Modifying
    @Query("UPDATE CityEntity c SET c.state = :state WHERE c.id = :id")
    void updateCityById(@Param("id") final long id, @Param("state") final String state);
}
