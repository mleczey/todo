package com.mleczey.todo.task.entity.command;

import java.util.function.Function;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class CreateTaskRequestTest {

  @Test
  void shouldBeInvalidWhenNoTitle() {
    // given
    final var testedObject = CreateTaskRequest.builder().build();
    final var validator = validator();

    // when
    final var actual = validator.validate(testedObject);

    // then
    assertThat(actual)
        .extracting(property(), annotation())
        .contains(tuple("title", NotNull.class));
  }

  private Validator validator() {
    return Validation.buildDefaultValidatorFactory().getValidator();
  }

  private Function<ConstraintViolation<?>, String> property() {
    return violation -> violation.getPropertyPath().toString();
  }

  private Function<ConstraintViolation<?>, Class<?>> annotation() {
    return violation -> violation.getConstraintDescriptor().getAnnotation().annotationType();
  }
}
