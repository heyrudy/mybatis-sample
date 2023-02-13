package com.heyrudy.mybatissample.app.api.internal.handlers.dbstorage.spring.entity.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CityResponseDto {

    String name;
    String state;
    String country;
}
