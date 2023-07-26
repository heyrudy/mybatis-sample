package com.heyrudy.mybatissample.domain.model.common;


public record CityCriteriaDTO(long cityId) {

    public static CityCriteriaDTOBuilder builder() {
        return new CityCriteriaDTOBuilder();
    }

    public static class CityCriteriaDTOBuilder {
        private long cityId;

        public CityCriteriaDTOBuilder() {
        }

        public CityCriteriaDTOBuilder cityId(long cityId) {
            this.cityId = cityId;
            return this;
        }

        public CityCriteriaDTO build() {
            return new CityCriteriaDTO(cityId);
        }
    }
}
