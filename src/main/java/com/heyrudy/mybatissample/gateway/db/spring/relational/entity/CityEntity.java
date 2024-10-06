package com.heyrudy.mybatissample.gateway.db.spring.relational.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "city")
@Entity
public class CityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String state;
    private String country;

    public CityEntity() {
    }

    public CityEntity(long id, String name, String state, String country) {
        this.id = id;
        this.name = name;
        this.state = state;
        this.country = country;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public static CityEntityBuilder builder() {
        return new CityEntityBuilder();
    }

    public static class CityEntityBuilder {

        long id;
        private String name;
        private String state;
        private String country;

        public CityEntityBuilder() {
            super();
        }

        public CityEntityBuilder id(long id) {
            this.id = id;
            return this;
        }

        public CityEntityBuilder name(String name) {
            this.name = name;
            return this;
        }

        public CityEntityBuilder state(String state) {
            this.state = state;
            return this;
        }

        public CityEntityBuilder country(String country) {
            this.country = country;
            return this;
        }

        public CityEntity build() {
            return new CityEntity(id, name, state, country);
        }
    }
}
