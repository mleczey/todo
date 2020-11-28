package com.mleczey.todo.task.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import java.util.Locale;
import java.util.regex.Pattern;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import static java.util.Objects.requireNonNull;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@JsonAutoDetect(fieldVisibility = Visibility.ANY)
@ToString
public final class TagName {

  private static final String REGEXP = "[\\pL\\pN _]+";

  private static final Pattern PATTERN = Pattern.compile(REGEXP);

  static final int MAX_LENGTH = 64;

  @NotBlank(message = "Tag name is mandatory.")
  @javax.validation.constraints.Pattern(regexp = REGEXP, message = "Tag name must contain only: letters, numbers, spaces and '_'.")
  @Size(max = MAX_LENGTH, message = "Given tag name with length '${validatedValue.length()}' is greater than maximum allowed length '{max}'.")
  private final String name;

  public String value() {
    return name;
  }

  public static TagName from(final String tagName) {
    if (requireNonNull(tagName, "Name can not be null.").isBlank()) {
      throw new IllegalArgumentException("Name can not be blank.");
    }

    if (MAX_LENGTH < tagName.length()) {
      throw new IllegalArgumentException("Tag name length can not be greater than " + MAX_LENGTH + ".");
    }

    if (!PATTERN.matcher(tagName).matches()) {
      throw new IllegalArgumentException("Tag name must contain only: letters, numbers, spaces and '_'.");
    }

    return new TagName(StringUtils.stripAccents(tagName).toLowerCase(Locale.ENGLISH));
  }
}
