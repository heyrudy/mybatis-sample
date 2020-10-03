package com.heyrudy.mybatissample.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Getter
@Setter
@ToString(of = {"name", "state", "country"})
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class City implements Serializable {

    long cityId;
    String name;
    String state;
    String country;
}
