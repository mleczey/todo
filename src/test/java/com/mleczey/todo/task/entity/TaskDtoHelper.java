package com.mleczey.todo.task.entity;

import com.github.javafaker.Faker;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Arrays;
import static java.util.stream.Collectors.toSet;

public final class TaskDtoHelper {

  public static final TaskId TASK_ID = TaskId.from("72c949be-1fbd-451d-bf18-5c436fae8a68");

  public static final ZonedDateTime CREATED_AT = ZonedDateTime.of(2000, 1, 1, 12, 0, 0, 0, ZoneOffset.UTC);

  public static final ZonedDateTime MODIFIED_AT = ZonedDateTime.of(2000, 1, 1, 12, 0, 0, 0, ZoneOffset.UTC);

  private TaskDtoHelper() throws InstantiationException {
    throw new InstantiationException();
  }

  public static TaskDto setUpTask(final Faker faker) {
    return buildTask(faker).build();
  }

  private static TaskDto.TaskDtoBuilder buildTask(final Faker faker) {
    return TaskDto.builder()
        .taskId(TASK_ID)
        .createdAt(CREATED_AT)
        .modifiedAt(MODIFIED_AT)
        .title(TaskTitle.from(faker.lorem().word()))
        .description(TaskDescription.from(faker.lorem().sentence()));
  }

  public static TaskDto setUpTask(final Faker faker, final TaskId taskId) {
    return buildTask(faker)
        .taskId(taskId)
        .build();
  }

  public static TaskDto setUpTask(final Faker faker, final TagName... tagNames) {
    return buildTask(faker)
        .tags(Arrays.stream(tagNames)
            .map(tagName -> TagDto.builder().name(tagName).build())
            .collect(toSet()))
        .build();
  }

  public static TaskDto setUpFinishedTask(final Faker faker, final ZonedDateTime finishedAt) {
    return buildTask(faker)
        .finishedAt(finishedAt)
        .build();
  }

  public static TaskDto setUpArchivedTask(final Faker faker, final ZonedDateTime archivedAt) {
    return buildTask(faker)
        .archivedAt(archivedAt)
        .build();
  }
}
