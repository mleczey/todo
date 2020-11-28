package com.mleczey.todo.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class ObjectMapperConfigurationHelper {

  private ObjectMapperConfigurationHelper() throws InstantiationException {
    throw new InstantiationException();
  }

  public static ObjectMapper objectMapper() {
    return new ObjectMapperConfiguration().objectMapper();
  }
}
