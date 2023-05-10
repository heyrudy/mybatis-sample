package com.heyrudy.mybatissample.entrypoint.utils;

public interface Mapper<I, O> {

    I toEntity(O dto);

    O toDto(I entity);
}
