package com.mleczey.todo.task.entity;

import com.github.javafaker.Faker;
import javax.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class TaskDescriptionTest {

  @Test
  void shouldThrowExceptionWhenDescriptionIsTooLong() {
    // given
    final var faker = Faker.instance();
    final var description = faker.lorem().fixedString(TaskDescription.MAX_LENGTH + 1);

    // expected
    assertThatExceptionOfType(ConstraintViolationException.class).isThrownBy(() -> TaskDescription.from(description));
  }
}