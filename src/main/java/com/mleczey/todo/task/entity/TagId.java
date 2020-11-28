package com.mleczey.todo.task.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.mleczey.todo.task.entity.exception.MalformedTagIdException;
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
public final class TagId {

  @NotNull
  private final UUID uuid;

  public UUID value() {
    return uuid;
  }

  public static TagId random() {
    return new TagId(UUID.randomUUID());
  }

  public static TagId from(final String s) {
    try {
      return new TagId(UUID.fromString(s));
    } catch (final IllegalArgumentException x) {
      throw new MalformedTagIdException(s, x);
    }
  }

  public static TagId from(final UUID uuid) {
    return new TagId(uuid);
  }
}
