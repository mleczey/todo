package com.mleczey.todo.task.control;

import com.mleczey.todo.task.entity.TagName;
import com.mleczey.todo.task.entity.TaskDto;
import com.mleczey.todo.task.entity.TaskId;
import com.mleczey.todo.task.entity.command.CreateTaskRequest;
import com.mleczey.todo.task.entity.command.UpdateTaskRequest;
import java.util.Collection;

public interface TaskService {

  TaskDto create(final CreateTaskRequest request);

  TaskDto find(final TaskId taskId);

  Collection<TaskDto> findAll();

  TaskDto update(final TaskId taskId, final UpdateTaskRequest request);

  TaskDto markAsFinished(final TaskId taskId);

  void delete(final TaskId taskId);

  TaskDto markAsArchived(final TaskId taskId);

  TaskDto addTagToTask(final TaskId taskId, final TagName tagName);

  TaskDto removeTagFromTask(final TaskId taskId, final TagName tagName);
}
