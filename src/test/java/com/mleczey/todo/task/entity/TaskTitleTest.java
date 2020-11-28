package com.mleczey.todo.task.entity;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class TaskTitleTest {
  @ParameterizedTest
  @ValueSource(strings = {"", "   "})
  void shouldThrowExceptionWhenTitleIsBlank(final String taskTitle) {
    // expected
    assertThatIllegalArgumentException().isThrownBy(() -> TaskTitle.from(taskTitle));
  }

  @Test
  void shouldThrowExceptionWhenTitleIsTooLong() {
    // given
    final var faker = Faker.instance();
    final var taskTitle = faker.lorem().fixedString(TaskTitle.MAX_LENGTH + 1);

    // expected
    assertThatIllegalArgumentException().isThrownBy(() -> TaskTitle.from(taskTitle));
  }
}