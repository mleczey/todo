package com.mleczey.todo.task.control;

import com.mleczey.todo.task.entity.Task;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface TaskDataAccess {

  Task saveAndFlush(final Task task);

  Optional<Task> findByUuid(final UUID uuid);

  Collection<Task> findAll();

  void deleteByUuid(final UUID uuid);
}
