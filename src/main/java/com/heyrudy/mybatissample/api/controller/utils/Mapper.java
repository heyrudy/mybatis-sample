package com.heyrudy.mybatissample.api.controller.utils;

public interface Mapper<I, O> {

    I toEntity(O dto);

    O toDto(I entity);
}
