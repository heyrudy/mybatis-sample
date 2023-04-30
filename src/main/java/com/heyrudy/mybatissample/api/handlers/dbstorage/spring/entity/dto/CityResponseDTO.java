package com.heyrudy.mybatissample.api.handlers.dbstorage.spring.entity.dto;

import lombok.*;

@Builder
public record CityResponseDTO(String name, String state, String country) {
}
