package com.mleczey.todo.task.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import static java.util.Objects.requireNonNull;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
@ToString
public final class TaskTitle {

  static final int MAX_LENGTH = 256;

  @NotBlank(message = "Task title is mandatory.")
  @Size(max = MAX_LENGTH, message = "Given task title with length '${validatedValue.length()}' is greater than maximum allowed length '{max}'.")
  private final String title;

  public String value() {
    return title;
  }

  @JsonCreator
  public static TaskTitle from(@JsonProperty("title") final String title) {
    if (requireNonNull(title, "Title cannot be null.").isBlank()) {
      throw new IllegalArgumentException("Title cannot be blank.");
    }

    if (MAX_LENGTH < title.length()) {
      throw new IllegalArgumentException("Title length cannot be greater than " + MAX_LENGTH + ".");
    }

    return new TaskTitle(title);
  }
}
