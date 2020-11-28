package com.mleczey.todo.task.entity.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MalformedTaskIdException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public MalformedTaskIdException(final String s, final IllegalArgumentException x) {
    super("Tried to parse task id " + s + ".", x);
  }

}
