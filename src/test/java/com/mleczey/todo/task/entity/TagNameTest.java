package com.mleczey.todo.task.entity;

import com.github.javafaker.Faker;
import java.util.stream.Stream;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class TagNameTest {

  @Test
  void shouldThrowExceptionWhenTagNameIsNull() {
    // expected
    assertThatNullPointerException().isThrownBy(() -> TagName.from(null));
  }

  @ParameterizedTest
  @ValueSource(strings = {"", "   "})
  void shouldThrowExceptionWhenTagNameIsNullOrBlank(final String tagName) {
    // expected
    assertThatIllegalArgumentException().isThrownBy(() -> TagName.from(tagName));
  }

  @Test
  void shouldThrowExceptionWhenTagNameIsTooLong() {
    // given
    final var faker = Faker.instance();
    final var tagName = faker.lorem().fixedString(TagName.MAX_LENGTH + 1);

    // expected
    assertThatIllegalArgumentException().isThrownBy(() -> TagName.from(tagName));
  }

  @ParameterizedTest
  @ValueSource(strings = {"♥", "#", "-"})
  void shouldAllowOnlyAllowedSigns(final String tagName) {
    // expected
    assertThatIllegalArgumentException().isThrownBy(() -> TagName.from(tagName));
  }

  @MethodSource("namesWithSpecialCharactersProvider")
  @ParameterizedTest(name = "Should normalize {0} tag name to {1}.")
  void shouldNormalizeTagName(final String tagName, final String expected) {
    // when
    final var actual = TagName.from(tagName);

    // then
    assertThat(actual.value()).isEqualTo(expected);
  }

  static Stream<Arguments> namesWithSpecialCharactersProvider() {
    return Stream.of(
        arguments("tag name", "tag name"),
        arguments("Tag Name", "tag name"),
        arguments("Tąg Ńamę", "tag name"),
        arguments("Tag_name 4", "tag_name 4")
    );
  }
}
