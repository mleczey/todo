package com.mleczey.todo.task.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.mleczey.todo.task.entity.exception.MalformedTaskIdException;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
@ToString
public final class TaskId {

  @NotNull
  private final UUID uuid;

  public UUID value() {
    return uuid;
  }

  public static TaskId random() {
    return new TaskId(UUID.randomUUID());
  }

  public static TaskId from(final String s) {
    try {
      return new TaskId(UUID.fromString(s));
    } catch (final IllegalArgumentException x) {
      throw new MalformedTaskIdException(s, x);
    }
  }

  public static TaskId from(final UUID uuid) {
    return new TaskId(uuid);
  }
}
