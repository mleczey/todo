package com.mleczey.todo.task.boundary;

import com.mleczey.todo.task.control.TaskService;
import com.mleczey.todo.task.entity.TaskDto;
import com.mleczey.todo.task.entity.TaskId;
import com.mleczey.todo.task.entity.command.CreateTaskRequest;
import com.mleczey.todo.task.entity.command.UpdateTaskRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.util.Collection;
import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@AllArgsConstructor
@RequestMapping("/resources/tasks")
@RestController
public class TaskResource {

  private final TaskService taskService;

  @ApiResponse(
      description = "When object is created.",
      headers = @Header(name = "Location", required = true),
      responseCode = "201")
  @ApiResponse(
      description = "When title or description do not meet requirements.",
      responseCode = "404")
  @Operation(description = "Create task given title and description.", summary = "Create task.")
  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Void> createTask(@RequestBody @Valid final CreateTaskRequest request, final UriComponentsBuilder builder) {
    final var task = taskService.create(request);
    final var uri = builder.path("/resources/tasks/{taskId}").buildAndExpand(task.getTaskId().value());
    final var header = new HttpHeaders();
    header.setLocation(uri.toUri());
    return new ResponseEntity<>(header, HttpStatus.CREATED);
  }

  @ApiResponse(
      description = "When returning tasks.",
      responseCode = "200")
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(description = "Get all tasks regardless of status.", summary = "Get all tasks.")
  @RolesAllowed("task-view")
  public ResponseEntity<Collection<TaskDto>> getTasks() {
    return ResponseEntity.ok(taskService.findAll());
  }

  @ApiResponse(
      description = "When task exists.",
      responseCode = "200")
  @ApiResponse(
      description = "When task does not exist.",
      responseCode = "404")
  @GetMapping(value = "/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @Operation(description = "Get tasks regardless of status.", summary = "Get tasks.")
  public ResponseEntity<TaskDto> getTask(@PathVariable("taskId") final String taskId) {
    return ResponseEntity.ok(taskService.find(TaskId.from(taskId)));
  }

  @ApiResponse(
      description = "When task exists and is updated.",
      responseCode = "200")
  @ApiResponse(
      description = "When task does not exist or title, description do not meet requirements.",
      responseCode = "404")
  @Operation(description = "Update task with given title and description.", summary = "Update task.")
  @PutMapping(value = "/{taskId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<TaskDto> updateTask(@PathVariable("taskId") final String taskId, @RequestBody @Valid final UpdateTaskRequest request) {
    return ResponseEntity.ok(taskService.update(TaskId.from(taskId), request));
  }

  @ApiResponse(
      description = "When task was marked as finished.",
      responseCode = "200")
  @ApiResponse(
      description = "When task does not exist.",
      responseCode = "404")
  @Operation(description = "Marks now task as finished.", summary = "Mark task as finished.")
  @PutMapping(value = "/{taskId}/finish", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<TaskDto> markAsFinished(@PathVariable("taskId") final String taskId) {
    return ResponseEntity.ok(taskService.markAsFinished(TaskId.from(taskId)));
  }

  @ApiResponse(
      description = "When task was deleted.",
      responseCode = "204")
  @DeleteMapping("/{taskId}")
  @Operation(description = "Deletes task with given taskId.", summary = "Delete task.")
  public ResponseEntity<Void> deleteTask(@PathVariable("taskId") final String taskId) {
    taskService.delete(TaskId.from(taskId));
    return ResponseEntity.noContent().build();
  }

  @ApiResponse(
      description = "When task was marked as archived.",
      responseCode = "200")
  @ApiResponse(
      description = "When task does not exist.",
      responseCode = "404")
  @Operation(description = "Marks now task as archived.", summary = "Mark task as archived.")
  @PutMapping(value = "/{taskId}/archive", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<TaskDto> markAsArchived(@PathVariable("taskId") final String taskId) {
    return ResponseEntity.ok(taskService.markAsArchived(TaskId.from(taskId)));
  }
}
