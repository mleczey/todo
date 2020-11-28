package com.mleczey.todo.task.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.javafaker.Faker;
import com.mleczey.todo.configuration.ObjectMapperConfigurationHelper;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

class TaskDtoTest {

  @Test
  void shouldMapToJson() throws JsonProcessingException, JSONException {
    // given
    final var faker = Faker.instance();
    final var uuid = UUID.randomUUID();
    final var dateTime = ZonedDateTime.of(2000, 1, 1, 12, 0, 0, 0, ZoneOffset.UTC);
    final var title = faker.lorem().word();
    final var taskDescription = faker.lorem().sentence();
    final var tagName = faker.lorem().word();
    final var tagDescription = faker.lorem().sentence();
    final var taskDto = taskDto(uuid, dateTime, title, taskDescription, tagDto(tagName, tagDescription));

    final var expected = new JSONObject()
        .put("uuid", uuid)
        .put("createdAt", "2000-01-01T12:00:00Z")
        .put("modifiedAt", "2000-01-01T12:00:00Z")
        .put("title", title)
        .put("description", taskDescription)
        .put("finishedAt", "2000-01-01T12:00:00Z")
        .put("archivedAt", "2000-01-01T12:00:00Z")
        .put("tags", new JSONArray().put(
            new JSONObject()
                .put("name", tagName)
                .put("description", tagDescription))
        );

    // when
    final var actual = ObjectMapperConfigurationHelper.objectMapper().writeValueAsString(taskDto);

    // then
    JSONAssert.assertEquals(expected.toString(), actual, JSONCompareMode.STRICT);
  }

  private TaskDto taskDto(final UUID uuid, final ZonedDateTime dateTime, final String title, final String description, final TagDto... tagDtos) {
    return TaskDto.builder()
        .taskId(TaskId.from(uuid))
        .createdAt(dateTime)
        .modifiedAt(dateTime)
        .title(TaskTitle.from(title))
        .description(TaskDescription.from(description))
        .finishedAt(dateTime)
        .archivedAt(dateTime)
        .tags(Set.of(tagDtos))
        .build();
  }

  private TagDto tagDto(final String tagName, final String description) {
    return TagDto.builder()
        .name(TagName.from(tagName))
        .description(TagDescription.from(description))
        .build();
  }
}
