package com.mleczey.todo.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

class ObjectMapperConfigurationTest {

  @Test
  void shouldParseZonedDateTimeWithoutMilliseconds() throws JsonProcessingException {
    // given
    final var dateTime = ZonedDateTime.of(2020, 10, 10, 10, 10, 10, 10, ZoneOffset.UTC);
    final var testedObject = new ObjectMapperConfiguration().objectMapper();

    // when
    final var actual = testedObject.writeValueAsString(dateTime);

    // then
    assertThat(actual).isEqualTo("\"2020-10-10T10:10:10Z\"");
  }

}
