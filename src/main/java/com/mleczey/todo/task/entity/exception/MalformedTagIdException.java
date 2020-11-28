package com.mleczey.todo.task.entity.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MalformedTagIdException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public MalformedTagIdException(final String s, final IllegalArgumentException x) {
    super("Tried to parse tag id " + s + ".", x);
  }
}
