package com.mleczey.todo.task.entity.exception;

import com.mleczey.todo.task.entity.TagName;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TagNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public TagNotFoundException(final TagName tagName) {
    super("Tag could not be found. Name: " + tagName.value());
  }

}
