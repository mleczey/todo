package com.mleczey.todo.task.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import javax.annotation.Nullable;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@ToString
public class TagDescription {

  static final int MAX_LENGTH = 256;

  @Nullable
  @Size(max = MAX_LENGTH)
  private final String description;

  public String value() {
    return description;
  }

  public static TagDescription from(final String tagDescription) {
    if (null != tagDescription && tagDescription.length() > MAX_LENGTH) {
      throw new IllegalArgumentException("Description length cannot be greater than " + MAX_LENGTH + ".");
    }

    return new TagDescription(tagDescription);
  }
}
