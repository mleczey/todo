package com.mleczey.todo.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ObjectMapperConfiguration {

  private static final DateTimeFormatter ISO8601 = new DateTimeFormatterBuilder().appendInstant(0).toFormatter();

  @Bean
  ObjectMapper objectMapper() {
    final var module = new JavaTimeModule();
    final var serializer = new ZonedDateTimeSerializer(ISO8601);
    module.addSerializer(ZonedDateTime.class, serializer);
    module.addDeserializer(ZonedDateTime.class, InstantDeserializer.ZONED_DATE_TIME);

    return new ObjectMapper()
        .registerModule(module)
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
  }
}
