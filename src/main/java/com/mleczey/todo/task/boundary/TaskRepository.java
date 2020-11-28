package com.mleczey.todo.task.boundary;

import com.mleczey.todo.task.control.TaskDataAccess;
import com.mleczey.todo.task.entity.Task;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.EntityGraph.EntityGraphType;
import org.springframework.data.jpa.repository.JpaRepository;

interface TaskRepository extends JpaRepository<Task, Long>, TaskDataAccess {

  @EntityGraph(value = "Task.tags", type = EntityGraphType.FETCH)
  @Override
  Optional<Task> findByUuid(final UUID uuid);

  @EntityGraph(value = "Task.tags", type = EntityGraphType.FETCH)
  @Override
  List<Task> findAll();
}
