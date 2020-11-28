package com.mleczey.todo.task.entity;

import com.mleczey.todo.task.entity.command.CreateTaskRequest;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
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
import static java.util.stream.Collectors.toSet;
import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
@NamedEntityGraph(name = "Task.tags",
    attributeNodes = @NamedAttributeNode("tags"))
@ToString
public class Task {

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
  private final UUID uuid;

  @NotNull
  @PastOrPresent
  private final ZonedDateTime createdAt;

  @NotNull
  @PastOrPresent
  private ZonedDateTime modifiedAt;

  @NotBlank
  @Size(max = TaskTitle.MAX_LENGTH)
  private String title;

  @Nullable
  @Size(max = TaskDescription.MAX_LENGTH)
  private String description;

  @Nullable
  @PastOrPresent
  private ZonedDateTime finishedAt;

  @Nullable
  @PastOrPresent
  private ZonedDateTime archivedAt;

  @JoinTable(name = "TASK_TAG",
      joinColumns = @JoinColumn(name = "TASK_ID"),
      inverseJoinColumns = @JoinColumn(name = "TAG_ID"))
  @ManyToMany(cascade = {MERGE, PERSIST}, fetch = LAZY)
  @ToString.Exclude
  private Set<Tag> tags;

  private Task() {
    id = null;
    version = null;
    uuid = TaskId.random().value();
    createdAt = now(ZoneOffset.UTC);
    modifiedAt = createdAt;
    finishedAt = null;
    archivedAt = null;
    tags = new HashSet<>();
  }

  private Task(final String title, final String description) {
    this();
    this.title = title;
    this.description = description;
  }

  public TaskId getId() {
    return TaskId.from(uuid);
  }

  public TaskTitle getTitle() {
    return TaskTitle.from(title);
  }

  public Optional<TaskDescription> getDescription() {
    return null == description
        ? Optional.empty()
        : Optional.of(TaskDescription.from(description));
  }

  public Collection<Tag> getTags() {
    return Collections.unmodifiableSet(tags);
  }

  public Task update(final TaskTitle title, final TaskDescription description) {
    requireNonNull(title);

    boolean modified = false;

    if (!Objects.equals(this.title, title.value())) {
      this.title = title.value();
      modified = true;
    }

    if (null != this.description && null == description) {
      this.description = null;
      modified = true;
    } else if (!Objects.equals(this.description, description.value())) {
      this.description = description.value();
      modified = true;
    }

    if (modified) {
      modifiedAt = now(ZoneOffset.UTC);
    }

    return this;
  }

  public Task markAsFinished() {
    if (null == finishedAt) {
      finishedAt = now(ZoneOffset.UTC);
    }
    return this;
  }

  public Task markAsArchived() {
    if (null == archivedAt) {
      archivedAt = now(ZoneOffset.UTC);
    }
    return this;
  }

  public Task addTag(final Tag tag) {
    tags.add(tag);
    tag.addTask(this);
    return this;
  }

  public boolean hasTag(final Tag tag) {
    return tags.contains(tag);
  }

  public Task removeTag(final Tag tag) {
    tags.remove(tag);
    tag.removeTask(this);
    return this;
  }

  public TaskDto toDto() {
    return TaskDto.builder()
        .taskId(TaskId.from(uuid))
        .createdAt(createdAt)
        .modifiedAt(modifiedAt)
        .title(TaskTitle.from(title))
        .description(null == description ? null : TaskDescription.from(description))
        .finishedAt(finishedAt)
        .archivedAt(archivedAt)
        .tags(tags.stream()
            .map(Tag::toDto)
            .collect(toSet()))
        .build();
  }

  public static Task create(final TaskTitle title, final TaskDescription description) {
    return new Task(requireNonNull(title).value(), null == description ? null : description.value());
  }

  public static Task from(final CreateTaskRequest request) {
    return create(request.getTitle(), request.getDescription());
  }
}
