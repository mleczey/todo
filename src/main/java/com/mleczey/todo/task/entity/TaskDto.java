package com.mleczey.todo.task.entity;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Value
public class TaskDto {

  @JsonUnwrapped
  @NotNull
  private final TaskId taskId;

  @NotNull
  @PastOrPresent
  private final ZonedDateTime createdAt;

  @NotNull
  @PastOrPresent
  private final ZonedDateTime modifiedAt;

  @JsonUnwrapped
  @NotNull
  private final TaskTitle title;

  @JsonUnwrapped
  @Nullable
  private final TaskDescription description;

  @Nullable
  @PastOrPresent
  private final ZonedDateTime finishedAt;

  @Nullable
  @PastOrPresent
  private final ZonedDateTime archivedAt;

  @Builder.Default
  private final Set<TagDto> tags = new HashSet<>();
}
