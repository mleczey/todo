package com.mleczey.todo.task.boundry;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.mleczey.todo.task.control.TaskService;
import com.mleczey.todo.task.entity.TaskDescription;
import com.mleczey.todo.task.entity.TaskDto;
import com.mleczey.todo.task.entity.command.CreateTaskRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.mleczey.todo.task.entity.TaskDtoHelper.TASK_ID;
import static com.mleczey.todo.task.entity.TaskDtoHelper.setUpTask;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class TaskResourceIntegrationTest {

  @MockBean
  private TaskService taskService;

  @Test
  void shouldCreateTask(@Autowired final MockMvc mockMvc, @Autowired final ObjectMapper objectMapper) throws Exception {
    // given
    final var faker = Faker.instance();
    final var task = setUpTask(faker);
    final var request = createTaskRequest(task);

    given(taskService.create(request))
        .willReturn(task);

    // when
    final var actual = mockMvc.perform(post("/resources/tasks")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)));

    // then
    actual.andExpect(status().isCreated())
        .andExpect(header().string(HttpHeaders.LOCATION, "http://localhost/resources/tasks/" + TASK_ID.value()));
  }

  @Test
  void shouldReturnBadRequestWhenRequestDoesNotFulfillCreateContract(@Autowired final MockMvc mockMvc, @Autowired final ObjectMapper objectMapper) throws Exception {
    // given
    final var faker = Faker.instance();
    final var description = faker.lorem().fixedString(TaskDescription.MAX_LENGTH + 1);
//    final var request = CreateTaskRequest.builder()
//        .description(TaskDescription.from(faker.lorem().characters(1025, 1026, true, false)))
//        .build();
    final var request = """
        {
          "description": "$description"
        }
        """.replace("$description", description);

    // when
    final var actual = mockMvc.perform(post("/resources/tasks")
        .contentType(MediaType.APPLICATION_JSON)
//        .content(objectMapper.writeValueAsString(request)));
        .content(request));

    // then
    actual.andExpect(status().isBadRequest())
//        .andExpect(jsonPath("$.errors.title[0]", equalTo("Title is mandatory.")))
        .andExpect(jsonPath("$.errors.description[0]", equalTo("Given description with length '1025' is greater than maximum allowed length '1024'.")));
  }
//
//  @Test
//  void shouldGetTask() throws Exception {
//    // given
//    final var tagName = TagName.from(faker.lorem().word());
//    final var task = setUpTask(faker, tagName);
//    final var taskid = task.getTaskId();
//
//    given(taskService.find(taskid))
//        .willReturn(task);
//
//    // when
//    final var actual = mockMvc.perform(get("/resources/tasks/" + taskid.value()));
//
//    // then
//    actual.andExpect(status().isOk())
//        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//        .andExpect(jsonPath("$.uuid", equalTo(taskid.value().toString())))
//        .andExpect(jsonPath("$.createdAt", equalTo("2000-01-01T12:00:00Z")))
//        .andExpect(jsonPath("$.modifiedAt", equalTo("2000-01-01T12:00:00Z")))
//        .andExpect(jsonPath("$.title", equalTo(task.getTitle())))
//        .andExpect(jsonPath("$.description", equalTo(task.getDescription())))
//        .andExpect(jsonPath("$.finishedAt").doesNotExist())
//        .andExpect(jsonPath("$.archivedAt").doesNotExist())
//        .andExpect(jsonPath("$.tags[*].name", contains(tagName.value())));
//  }
//
//  @Test
//  void shouldReturnNotFoundWhenTaskDoesNotExist() throws Exception {
//    // given
//    given(taskService.find(TASK_ID))
//        .willThrow(new TaskNotFoundException(TASK_ID));
//
//    // when
//    final var actual = mockMvc.perform(get("/resources/tasks/" + TASK_ID.value()));
//
//    // then
//    actual.andExpect(status().isNotFound());
//  }
//
//  @Test
//  void shouldGetTasks() throws Exception {
//    // given
//    given(taskService.findAll())
//        .willReturn(List.of(
//            setUpTask(faker, TaskId.random()),
//            setUpTask(faker, TaskId.random())));
//
//    // when
//    final var actual = mockMvc.perform(get("/resources/tasks"));
//
//    // then
//    actual.andExpect(status().isOk())
//        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//        .andExpect(jsonPath("$", hasSize(2)));
//  }
//
//  @Test
//  void shouldUpdateTask() throws Exception {
//    // given
//    final var task = setUpTask(faker);
//    final var taskId = task.getTaskId();
//    final var request = UpdateTaskRequest.builder()
//        .title(TaskTitle.from(faker.lorem().word()))
//        .description(TaskDescription.from(faker.lorem().sentence()))
//        .build();
//
//    given(taskService.update(taskId, request))
//        .willReturn(task);
//
//    // when
//    final var actual = mockMvc.perform(put("/resources/tasks/" + taskId.value())
//        .contentType(MediaType.APPLICATION_JSON)
//        .content(objectMapper.writeValueAsString(request)));
//
//    // then
//    actual.andExpect(jsonPath("$.uuid", equalTo(taskId.value().toString())))
//        .andExpect(jsonPath("$.createdAt", equalTo("2000-01-01T12:00:00Z")))
//        .andExpect(jsonPath("$.modifiedAt", equalTo("2000-01-01T12:00:00Z")))
//        .andExpect(jsonPath("$.title", equalTo(task.getTitle())))
//        .andExpect(jsonPath("$.description", equalTo(task.getDescription())))
//        .andExpect(jsonPath("$.finishedAt").doesNotExist())
//        .andExpect(jsonPath("$.archivedAt").doesNotExist());
//  }
//
//  @Test
//  void shouldRetrunNotFoundWhenTaskDoesNotExistDuringUpdate() throws Exception {
//    // given
//    final var request = UpdateTaskRequest.builder()
//        .title(TaskTitle.from(faker.lorem().word()))
//        .description(TaskDescription.from(faker.lorem().sentence()))
//        .build();
//
//    given(taskService.update(TASK_ID, request))
//        .willThrow(new TaskNotFoundException(TASK_ID));
//
//    // when
//    final var actual = mockMvc.perform(put("/resources/tasks/" + TASK_ID.value())
//        .contentType(MediaType.APPLICATION_JSON)
//        .content(objectMapper.writeValueAsString(request)));
//
//    // then
//    actual.andExpect(status().isNotFound());
//  }
//
//  @Test
//  void shouldReturnBadRequestWhenRequestDoesNotFulfillUpdateContract() throws Exception {
//    // given
//    final var request = UpdateTaskRequest.builder()
//        .description(TaskDescription.from(faker.lorem().characters(1025, 1026, true, false)))
//        .build();
//
//    // when
//    final var actual = mockMvc.perform(put("/resources/tasks/" + TASK_ID.value())
//        .contentType(MediaType.APPLICATION_JSON)
//        .content(objectMapper.writeValueAsString(request)));
//
//    // then
//    actual.andExpect(status().isBadRequest())
//        .andExpect(jsonPath("$.errors.title[0]", equalTo("Title is mandatory.")))
//        .andExpect(jsonPath("$.errors.description[0]", equalTo("Given description with length '1025' is greater than maximum allowed length '1024'.")));
//  }
//
//  @Test
//  void shouldMarkTaskAsFinished() throws Exception {
//    // given
//    final var finishedAt = ZonedDateTime.of(2000, 1, 1, 12, 0, 0, 0, ZoneOffset.UTC);
//    final var task = setUpFinishedTask(faker, finishedAt);
//    final var taskId = task.getTaskId();
//
//    given(taskService.markAsFinished(taskId))
//        .willReturn(task);
//
//    // when
//    final var actual = mockMvc.perform(put("/resources/tasks/" + taskId.value() + "/finish"));
//
//    // then
//    actual.andExpect(status().isOk())
//        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//        .andExpect(jsonPath("$.uuid", equalTo(taskId.value().toString())))
//        .andExpect(jsonPath("$.createdAt", equalTo("2000-01-01T12:00:00Z")))
//        .andExpect(jsonPath("$.modifiedAt", equalTo("2000-01-01T12:00:00Z")))
//        .andExpect(jsonPath("$.title", equalTo(task.getTitle())))
//        .andExpect(jsonPath("$.description", equalTo(task.getDescription())))
//        .andExpect(jsonPath("$.finishedAt", equalTo("2000-01-01T12:00:00Z")))
//        .andExpect(jsonPath("$.archivedAt").doesNotExist());
//  }
//
//  @Test
//  void shouldDeleteTask() throws Exception {
//    // when
//    final var actual = mockMvc.perform(delete("/resources/tasks/" + TASK_ID.value()));
//
//    // then
//    actual.andExpect(status().isNoContent());
//  }
//
//  @Test
//  void shouldMarkTaskAsArchived() throws Exception {
//    // given
//    final var archivedAt = ZonedDateTime.of(2000, 1, 1, 12, 0, 0, 0, ZoneOffset.UTC);
//    final var task = setUpArchivedTask(faker, archivedAt);
//    final var taskId = task.getTaskId();
//
//    given(taskService.markAsArchived(taskId))
//        .willReturn(task);
//
//    // when
//    final var actual = mockMvc.perform(put("/resources/tasks/" + taskId.value() + "/archive"));
//
//    // then
//    actual.andExpect(status().isOk())
//        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//        .andExpect(jsonPath("$.uuid", equalTo(taskId.value().toString())))
//        .andExpect(jsonPath("$.createdAt", equalTo("2000-01-01T12:00:00Z")))
//        .andExpect(jsonPath("$.modifiedAt", equalTo("2000-01-01T12:00:00Z")))
//        .andExpect(jsonPath("$.title", equalTo(task.getTitle())))
//        .andExpect(jsonPath("$.description", equalTo(task.getDescription())))
//        .andExpect(jsonPath("$.finishedAt").doesNotExist())
//        .andExpect(jsonPath("$.archivedAt", equalTo("2000-01-01T12:00:00Z")));
//  }

  private CreateTaskRequest createTaskRequest(final TaskDto task) {
    return CreateTaskRequest.builder()
        .title(task.getTitle())
        .description(task.getDescription())
        .build();
  }
}
