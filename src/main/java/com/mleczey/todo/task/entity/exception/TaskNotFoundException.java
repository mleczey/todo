package com.mleczey.todo.task.entity.exception;

import com.mleczey.todo.task.entity.TaskId;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TaskNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public TaskNotFoundException(final TaskId taskId) {
    super("Task could not be found. UUID: " + taskId.value());
  }
}
