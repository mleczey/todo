package com.mleczey.todo.task.control;

import com.mleczey.todo.task.entity.Task;
import com.mleczey.todo.task.entity.TaskDto;
import com.mleczey.todo.task.entity.TaskId;
import com.mleczey.todo.task.entity.command.CreateTaskRequest;
import com.mleczey.todo.task.entity.command.UpdateTaskRequest;
import com.mleczey.todo.task.entity.exception.TaskNotFoundException;
import java.util.Collection;
import static java.util.stream.Collectors.toList;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Slf4j
class TaskManager {

  private final TaskDataAccess taskDataAccess;

  @Transactional
  public TaskDto create(final CreateTaskRequest request) {
    log.debug("Attempting to create task from {}.", request);
    final var task = taskDataAccess.saveAndFlush(Task.from(request));
    log.info("Task {} was created.", task);
    return task.toDto();
  }

  @Transactional(readOnly = true)
  public TaskDto find(final TaskId taskId) {
    return taskDataAccess.findByUuid(taskId.value())
        .map(Task::toDto)
        .orElseThrow(() -> new TaskNotFoundException(taskId));
  }

  @Transactional(readOnly = true)
  public Collection<TaskDto> findAll() {
    return taskDataAccess.findAll()
        .stream()
        .map(Task::toDto)
        .collect(toList());
  }

  @Transactional
  public TaskDto update(final TaskId taskId, final UpdateTaskRequest request) {
    log.debug("Attempting to update task {} with {}.", taskId, request);
    return taskDataAccess.findByUuid(taskId.value())
        .map(task -> task.update(request.getTitle(), request.getDescription()))
        .map(taskDataAccess::saveAndFlush)
        .map(Task::toDto)
        .orElseThrow(() -> new TaskNotFoundException(taskId));
  }

  public TaskDto markAsFinished(final TaskId taskid) {
    log.debug("Attempting to mark task {} as finished.", taskid);
    return taskDataAccess.findByUuid(taskid.value())
        .map(Task::markAsFinished)
        .map(taskDataAccess::saveAndFlush)
        .map(Task::toDto)
        .orElseThrow(() -> new TaskNotFoundException(taskid));
  }

  @Transactional
  public void delete(final TaskId taskId) {
    log.debug("Attempting to delete task with taskId {}.", taskId);
    taskDataAccess.deleteByUuid(taskId.value());
    log.info("Task {} was deleted.", taskId);
  }

  public TaskDto markAsArchived(final TaskId taskId) {
    log.debug("Attempting to mark task {} as archived.", taskId);
    return taskDataAccess.findByUuid(taskId.value())
        .map(Task::markAsArchived)
        .map(taskDataAccess::saveAndFlush)
        .map(Task::toDto)
        .orElseThrow(() -> new TaskNotFoundException(taskId));
  }
}
