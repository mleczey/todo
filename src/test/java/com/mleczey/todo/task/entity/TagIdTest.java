package com.mleczey.todo.task.entity;

import com.mleczey.todo.task.entity.exception.MalformedTagIdException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import org.junit.jupiter.api.Test;

class TagIdTest {

  @Test
  void shouldCreateTagId() {
    // given
    final var uuid = UUID.randomUUID().toString();

    // when
    final var actual = TagId.from(uuid);

    // then
    assertThat(actual).isNotNull();
  }

  @Test
  void shouldThrowExceptionWhenUuidIsMalformed() {
    // given
    final var uuid = "malformed uuid";

    // expected
    assertThatExceptionOfType(MalformedTagIdException.class).isThrownBy(() -> TagId.from(uuid));
  }
}
