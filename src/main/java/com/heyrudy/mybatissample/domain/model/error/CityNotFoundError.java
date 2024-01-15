package com.heyrudy.mybatissample.domain.model.error;

public final class CityNotFoundError extends DomainError {

  public CityNotFoundError(String message) {
    super(message);
  }
}
