package com.mleczey.todo.task.entity.exception;

import com.mleczey.todo.task.entity.TagName;
import com.mleczey.todo.task.entity.TaskId;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TagNotAssignedToTaskException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public TagNotAssignedToTaskException(final TaskId taskId, final TagName tagName) {
    super("Task (UUID: " + taskId.value() + ") and tag (name: " + tagName.value() + ") have no relation.");
  }

}
