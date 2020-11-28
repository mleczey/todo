package com.mleczey.todo.task.control;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class TaskConfiguration {

  @Bean
  TaskService taskService(final TagDataAccess tagDataAccess, final TaskDataAccess taskDataAccess) {
    return new TaskFacade(
        new TaskManager(taskDataAccess),
        new TaskTagManager(tagDataAccess, taskDataAccess));
  }
}
