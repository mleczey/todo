package com.mleczey.todo.task.entity;

import com.mleczey.todo.task.entity.exception.MalformedTaskIdException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.Test;

class TaskIdTest {

  @Test
  void shouldCreateTaskId() {
    // given
    final var uuid = UUID.randomUUID().toString();

    // when
    final var actual = TaskId.from(uuid);

    // then
    assertThat(actual).isNotNull();
  }

  @Test
  void shouldThrowExceptionWhenUuidIsMalformed() {
    // given
    final var uuid = "malformed uuid";

    // expected
    assertThatExceptionOfType(MalformedTaskIdException.class).isThrownBy(() -> TaskId.from(uuid));
  }

}
