package com.mleczey.todo.task.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.javafaker.Faker;
import com.mleczey.todo.configuration.ObjectMapperConfigurationHelper;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

class TagDtoTest {

  @Test
  void shouldMapToJson() throws JsonProcessingException, JSONException {
    // given
    final var faker = Faker.instance();
    final var tagName = faker.lorem().word();
    final var description = faker.lorem().sentence();
    final var tagDto = TagDto.builder()
        .name(TagName.from(tagName))
        .description(TagDescription.from(description))
        .build();

    final var expected = new JSONObject()
        .put("name", tagName)
        .put("description", description);

    // when
    final var actual = ObjectMapperConfigurationHelper.objectMapper().writeValueAsString(tagDto);

    // then
    JSONAssert.assertEquals(expected.toString(), actual, JSONCompareMode.STRICT);
  }
}