package com.mleczey.todo.task.control;

import com.mleczey.todo.task.entity.TagName;
import com.mleczey.todo.task.entity.TaskDto;
import com.mleczey.todo.task.entity.TaskId;
import com.mleczey.todo.task.entity.command.CreateTaskRequest;
import com.mleczey.todo.task.entity.command.UpdateTaskRequest;
import java.util.Collection;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
class TaskFacade implements TaskService {

  private final TaskManager taskManager;

  private final TaskTagManager taskTagManager;

  @Override
  public TaskDto create(final CreateTaskRequest request) {
    return taskManager.create(request);
  }

  @Override
  public TaskDto find(final TaskId taskId) {
    return taskManager.find(taskId);
  }

  @Override
  public Collection<TaskDto> findAll() {
    return taskManager.findAll();
  }

  @Override
  public TaskDto update(final TaskId taskId, final UpdateTaskRequest request) {
    return taskManager.update(taskId, request);
  }

  @Override
  public TaskDto markAsFinished(final TaskId taskId) {
    return taskManager.markAsFinished(taskId);
  }

  @Override
  public void delete(final TaskId taskId) {
    taskManager.delete(taskId);
  }

  @Override
  public TaskDto markAsArchived(final TaskId taskId) {
    return taskManager.markAsArchived(taskId);
  }

  @Override
  public TaskDto addTagToTask(final TaskId taskId, final TagName tagName) {
    return taskTagManager.addTagToTask(taskId, tagName);
  }

  @Override
  public TaskDto removeTagFromTask(final TaskId taskId, final TagName tagName) {
    return taskTagManager.removeTagFromTask(taskId, tagName);
  }
}
