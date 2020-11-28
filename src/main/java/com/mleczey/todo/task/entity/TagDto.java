package com.mleczey.todo.task.entity;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Value
public class TagDto {

  @JsonUnwrapped
  @NotNull
  private final TagName name;

  @JsonUnwrapped
  @Nullable
  private final TagDescription description;
}
