package com.mleczey.todo.task.entity.command;

import com.mleczey.todo.task.entity.TaskDescription;
import com.mleczey.todo.task.entity.TaskTitle;
import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Data
@NoArgsConstructor
public class UpdateTaskRequest {

  @NotNull
  private TaskTitle title;

  @Nullable
  private TaskDescription description;
}
