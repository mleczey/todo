package com.mleczey.todo;

import java.util.List;
import java.util.Map;
import lombok.Value;

@Value
public class ErrorResponse {

  private final Map<String, List<String>> errors;
}
