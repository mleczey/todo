package com.mleczey.todo.task.boundary;

import com.mleczey.todo.task.control.TaskService;
import com.mleczey.todo.task.entity.TagName;
import com.mleczey.todo.task.entity.TaskDto;
import com.mleczey.todo.task.entity.TaskId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RequestMapping("/resources/tasks/{taskId}/tags/{name}")
@RestController
public class TaskTagResource {

  private final TaskService taskService;

  @ApiResponse(
      description = "When tag was added to task.",
      responseCode = "200")
  @ApiResponse(
      description = "When task does not exist.",
      responseCode = "404")
  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(description = "Adds tag with given name to task. If tag does not exist it will be created.", summary = "Add tag to task.")
  public ResponseEntity<TaskDto> addTagToTask(@PathVariable("taskId") final String taskId, @PathVariable("name") final String tagName) {
    return ResponseEntity.ok(taskService.addTagToTask(TaskId.from(taskId), TagName.from(tagName)));
  }

  @ApiResponse(
      description = "When tag was removed from task.",
      responseCode = "204")
  @ApiResponse(
      description = "When task does not exist or tag does not exist or was not added previously to given task.",
      responseCode = "404")
  @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(description = "Removes tag with given name from task with given taskId. Tag is never removed here permanently.", summary = "Remove tag from task.")
  public ResponseEntity<Void> removeTagFromTask(@PathVariable("taskId") final String taskId, @PathVariable("name") final String tagName) {
    taskService.removeTagFromTask(TaskId.from(taskId), TagName.from(tagName));
    return ResponseEntity.noContent().build();
  }
}
