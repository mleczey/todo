package com.mleczey.todo.task.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mleczey.todo.common.WithValidation;
import javax.annotation.Nullable;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
@ToString
public final class TaskDescription implements WithValidation<TaskDescription> {

  public static final int MAX_LENGTH = 1024;

  @Nullable
  @Size(max = MAX_LENGTH, message = "Given description with length '${validatedValue.length()}' is greater than maximum allowed length '{max}'.")
  private String description;

  public String value() {
    return description;
  }

  @JsonCreator
  public static TaskDescription from(@JsonProperty("description") final String description) {
    return new TaskDescription(description).validate();
  }
}
