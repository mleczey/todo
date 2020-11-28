package com.mleczey.todo.task.entity;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Version;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import static java.time.ZonedDateTime.now;
import static java.util.Objects.requireNonNull;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@ToString
public class Tag {

  @GeneratedValue(strategy = IDENTITY)
  @Id
  @ToString.Exclude
  private Long id;

  @Getter(AccessLevel.NONE)
  @ToString.Exclude
  @Version
  private Integer version;

  @EqualsAndHashCode.Include
  @NotNull
  private UUID uuid;

  @NotNull
  @PastOrPresent
  private ZonedDateTime createdAt;

  @NotBlank
  @Size(max = TagName.MAX_LENGTH)
  private String name;

  @Nullable
  @Size(max = TagDescription.MAX_LENGTH)
  private String description;

  @ManyToMany(fetch = LAZY, mappedBy = "tags")
  @ToString.Exclude
  private Set<Task> tasks;

  private Tag() {
    id = null;
    version = null;
    uuid = TagId.random().value();
    createdAt = now(ZoneOffset.UTC);
    tasks = new HashSet<>();
  }

  private Tag(final String name, final String description) {
    this();
    this.name = name;
    this.description = description;
  }

  public TagId getId() {
    return TagId.from(uuid);
  }

  public TagName getName() {
    return TagName.from(name);
  }

  public Optional<TagDescription> getDescription() {
    return null == description
        ? Optional.empty()
        : Optional.of(TagDescription.from(description));
  }

  void addTask(final Task task) {
    tasks.add(task);
  }

  void removeTask(final Task task) {
    tasks.remove(task);
  }

  public TagDto toDto() {
    return TagDto.builder()
        .name(TagName.from(name))
        .description(TagDescription.from(description))
        .build();
  }

  public static Tag create(final TagName tagName) {
    return create(tagName, null);
  }

  public static Tag create(final TagName tagName, final TagDescription description) {
    return new Tag(requireNonNull(tagName).value(), null == description ? null : description.value());
  }
}
