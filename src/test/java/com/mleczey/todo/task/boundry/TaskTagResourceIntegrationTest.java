package com.mleczey.todo.task.boundry;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.mleczey.todo.task.boundary.TaskTagResource;
import com.mleczey.todo.task.control.TaskService;
import com.mleczey.todo.task.entity.TagName;
import static com.mleczey.todo.task.entity.TaskDtoHelper.setUpTask;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.BDDMockito.given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(TaskTagResource.class)
class TaskTagResourceIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private TaskService taskService;

  private Faker faker = Faker.instance();

  @Test
  void shouldAddTagToTask() throws Exception {
    // given
    final var tagName = TagName.from(faker.lorem().word());
    final var task = setUpTask(faker, tagName);
    final var taskId = task.getTaskId();

    given(taskService.addTagToTask(taskId, tagName))
        .willReturn(task);

    // when
    final var actual = mockMvc.perform(post("/resources/tasks/" + taskId.value() + "/tags/" + tagName.value())
        .contentType(MediaType.APPLICATION_JSON));

    // then
    actual.andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.uuid", equalTo(taskId.value().toString())))
        .andExpect(jsonPath("$.createdAt", equalTo("2000-01-01T12:00:00Z")))
        .andExpect(jsonPath("$.modifiedAt", equalTo("2000-01-01T12:00:00Z")))
        .andExpect(jsonPath("$.title", equalTo(task.getTitle())))
        .andExpect(jsonPath("$.description", equalTo(task.getDescription())))
        .andExpect(jsonPath("$.finishedAt").doesNotExist())
        .andExpect(jsonPath("$.archivedAt").doesNotExist())
        .andExpect(jsonPath("$.tags[*].name", contains(tagName.value())));
  }

  @Test
  void shouldRemoveTagFromTask() throws Exception {
    // given
    final var tagName = TagName.from(faker.lorem().word());
    final var task = setUpTask(faker, tagName);
    final var taskId = task.getTaskId();

    given(taskService.removeTagFromTask(taskId, tagName))
        .willReturn(task);

    // when
    final var actual = mockMvc.perform(delete("/resources/tasks/" + taskId.value() + "/tags/" + tagName.value()));

    // then
    actual.andExpect(status().isNoContent());
  }
}
