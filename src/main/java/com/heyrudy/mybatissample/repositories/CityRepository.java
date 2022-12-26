package com.heyrudy.mybatissample.repositories;

import com.heyrudy.mybatissample.domain.City;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Mapper
public interface CityRepository {

    @Insert("INSERT INTO CITY (NAME, STATE, COUNTRY) VALUES(#{name}, #{state}, #{country})")
    void insertCity(City city);

    @Select("SELECT ID, NAME, STATE, COUNTRY FROM CITY WHERE ID = #{id}")
    @Result(column = "id", property = "cityId")
    Optional<City> findCityById(@Param("id") long id);

    @Select("SELECT * FROM CITY")
    @Result(column = "id", property = "cityId")
    List<City> findAllCities();
}
