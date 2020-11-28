package com.mleczey.todo.task.entity;

import com.github.javafaker.Faker;
import java.util.Optional;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.assertj.core.api.Assumptions.assumeThat;

class TaskTest {

  private final Faker faker = Faker.instance();

  @Test
  void shouldCreateTask() {
    // given
    final var title = title();
    final var description = description();

    // when
    final var actual = Task.create(title, description);

    // then
    final var softly = new SoftAssertions();
    softly.assertThat(actual)
        .hasTitle(TaskTitle.from(faker.lorem().word()))
        .hasDescription(Optional.of(TaskDescription.from(faker.lorem().word())));
    softly.assertAll();
  }

  @Test
  void shouldCreateTaskWithoutDescription() {
    // given
    final var title = title();
    final TaskDescription description = null;

    // when
    final var actual = Task.create(title, description);

    // then
    TaskAssert.assertThat(actual)
        .hasTitle(title);
  }

  @Test
  void shouldThrowExceptionWhenTitleIsNull() {
    // given
    final TaskTitle title = null;

    // expected
    assertThatNullPointerException().isThrownBy(() -> Task.create(title, description()));
  }

  @Test
  void shouldConvertToDto() {
    // given
    final var title = title();
    final var description = description();
    final var task = Task.create(title, description);

    final var expected = TaskDto.builder()
        .taskId(task.getId())
        .createdAt(task.getCreatedAt())
        .modifiedAt(task.getModifiedAt())
        .title(title)
        .description(description)
        .build();

    // when
    final var actual = task.toDto();

    // then
    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void shouldUpdateTask() {
    // given
    final var task = task();
    final var title = title();
    final var description = description();

    assumeThat(task.getCreatedAt()).isEqualTo(task.getModifiedAt());

    // when
    final var actual = task.update(title, description);

    // then
    TaskAssert.assertThat(actual)
        .hasTitle(title)
        .hasDescription(Optional.of(description));
    assertThat(actual.getCreatedAt()).isNotEqualTo(actual.getModifiedAt());
  }

  @Test
  void shouldUpdateTaskWithErasingDescription() {
    // given
    final var task = task();
    final var title = title();
    final TaskDescription description = null;

    assumeThat(task.getCreatedAt()).isEqualTo(task.getModifiedAt());

    // when
    final var actual = task.update(title, description);

    // then
    TaskAssert.assertThat(actual)
        .hasTitle(title)
        .hasDescription(Optional.empty());
    assertThat(actual.getCreatedAt()).isNotEqualTo(actual.getModifiedAt());
  }

  @Test
  void shouldMarkTaskAsFinished() {
    // given
    final var task = task();

    // when
    final var actual = task.markAsFinished();

    // then
    assertThat(actual.getFinishedAt()).isNotNull();
  }

  @Test
  void shouldNotChangeDateWhenWasOnceFinished() {
    // given
    final var task = task().markAsFinished();
    final var finishedAt = task.getFinishedAt();

    // when
    final var actual = task.markAsFinished();

    // then
    assertThat(actual.getFinishedAt()).isEqualTo(finishedAt);
  }

  @Test
  void shouldMarkTaskAsArchived() {
    // given
    final var task = task();

    // when
    final var actual = task.markAsArchived();

    // then
    assertThat(actual.getArchivedAt()).isNotNull();
  }

  @Test
  void shouldNotChangeDateWhenWasOnceArchived() {
    // given
    final var task = task().markAsArchived();
    final var archivedAt = task.getArchivedAt();

    // when
    final var actual = task.markAsArchived();

    // then
    assertThat(actual.getArchivedAt()).isEqualTo(archivedAt);
  }

  @Test
  void shouldBeDifferentWhenUuidsAreDifferent() {
    // given
    final var left = task();
    final var right = task();

    // expected
    assertThat(left).isNotEqualTo(right);
  }

  @Test
  void shouldNotChangeHashCodeWhenSomeValuesChange() {
    // given
    final var task = task();
    final var left = task.hashCode();
    final var right = task.markAsArchived().hashCode();

    // expected
    assertThat(left).isEqualTo(right);
  }

  @Test
  void shouldContainOnlySubsetOfFieldsInToStringMethod() {
    // given
    final var task = task().markAsFinished().markAsArchived();

    // when
    final var actual = task.toString();

    // then
    assertThat(actual)
        .contains("uuid=", "createdAt=", "modifiedAt=", "title=", "description=", "finishedAt=", "archivedAt=")
        .doesNotContainPattern("[^\\w]id=")
        .doesNotContain("version=", "tags=");
  }

  private TaskTitle title() {
    return TaskTitle.from(faker.lorem().word());
  }

  private TaskDescription description() {
    return TaskDescription.from(faker.lorem().sentence());
  }

  private Task task() {
    return Task.create(title(), description());
  }
}
