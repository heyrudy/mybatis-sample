package com.heyrudy.mybatissample.api.handlers.dbstorage.spring.entity.dto;

import lombok.Builder;

@Builder
public record CityRequestDTO(String name, String state, String country) {
}
