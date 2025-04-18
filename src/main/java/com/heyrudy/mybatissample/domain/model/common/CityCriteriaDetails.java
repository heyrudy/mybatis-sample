package com.heyrudy.mybatissample.domain.model.common;


public record CityCriteriaDetails(long cityId) {

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

        public CityCriteriaDetails build() {
            return new CityCriteriaDetails(cityId);
        }
    }
}
