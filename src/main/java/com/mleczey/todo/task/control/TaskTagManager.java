package com.mleczey.todo.task.control;

import com.mleczey.todo.task.entity.Tag;
import com.mleczey.todo.task.entity.TagName;
import com.mleczey.todo.task.entity.TaskDto;
import com.mleczey.todo.task.entity.TaskId;
import com.mleczey.todo.task.entity.exception.TagNotAssignedToTaskException;
import com.mleczey.todo.task.entity.exception.TagNotFoundException;
import com.mleczey.todo.task.entity.exception.TaskNotFoundException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Slf4j
class TaskTagManager {

  private final TagDataAccess tagDataAccess;

  private final TaskDataAccess taskDataAccess;

  @Transactional
  public TaskDto addTagToTask(final TaskId taskId, final TagName tagName) {
    log.debug("Attempting to assign tag {} to task {}.", tagName, taskId);
    final var task = taskDataAccess.findByUuid(taskId.value())
        .orElseThrow(() -> new TaskNotFoundException(taskId));

    final var tag = tagDataAccess.findByName(tagName.value())
        .orElseGet(() -> tagDataAccess.saveAndFlush(Tag.create(tagName)));
    task.addTag(tag);

    log.info("Tag {} was assigned to task {}.", tag, task);

    return task.toDto();
  }

  @Transactional
  public TaskDto removeTagFromTask(final TaskId taskId, final TagName tagName) {
    log.debug("Attempting to remove assignment from tag {} to task {}.", tagName, taskId);
    final var task = taskDataAccess.findByUuid(taskId.value())
        .orElseThrow(() -> new TaskNotFoundException(taskId));
    final var tag = tagDataAccess.findByName(tagName.value())
        .orElseThrow(() -> new TagNotFoundException(tagName));

    if (task.hasTag(tag)) {
      task.removeTag(tag);
    } else {
      throw new TagNotAssignedToTaskException(taskId, tagName);
    }

    log.info("Tag {} was unassigned from task {}.", tag, task);

    return task.toDto();
  }
}
