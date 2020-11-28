package com.mleczey.todo.common;

import javax.validation.ConstraintViolationException;
import javax.validation.Validation;

public interface WithValidation<T> {

  default T validate() {
    final var validatorFactory = Validation.buildDefaultValidatorFactory();
    final var validator = validatorFactory.getValidator();
    final var violations = validator.validate(this);
    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
    return (T) this;
  }
}
