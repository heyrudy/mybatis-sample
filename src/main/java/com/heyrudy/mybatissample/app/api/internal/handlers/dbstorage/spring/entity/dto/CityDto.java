package com.heyrudy.mybatissample.app.api.internal.handlers.dbstorage.spring.entity.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CityDto {

    String name;
    String state;
    String country;
}