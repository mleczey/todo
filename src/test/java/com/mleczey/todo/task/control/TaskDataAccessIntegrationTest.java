package com.mleczey.todo.task.control;

import com.github.javafaker.Faker;
import com.mleczey.todo.DataAccessTest;
import com.mleczey.todo.task.entity.Tag;
import com.mleczey.todo.task.entity.TagDescription;
import com.mleczey.todo.task.entity.TagName;
import com.mleczey.todo.task.entity.Task;
import com.mleczey.todo.task.entity.TaskAssert;
import com.mleczey.todo.task.entity.TaskDescription;
import com.mleczey.todo.task.entity.TaskTitle;
import java.util.Optional;
import org.flywaydb.core.Flyway;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;

import static com.mleczey.todo.StatisticsAssert.assertThat;
import static org.assertj.core.api.Assertions.assertThat;

class TaskDataAccessIntegrationTest extends DataAccessTest {

  @Container
  static MySQLContainer mySqlContainer = new MySQLContainer("mysql:8.0.21")
      .withDatabaseName("todo")
      .withUsername("admin")
      .withPassword("admin");

  @Autowired
  private TaskDataAccess taskDataAccess;

  private Faker faker = Faker.instance();

  @DynamicPropertySource
  static void mySqlProperites(final DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", mySqlContainer::getJdbcUrl);
    registry.add("spring.datasource.password", mySqlContainer::getPassword);
    registry.add("spring.datasource.username", mySqlContainer::getUsername);
  }

  @BeforeAll
  static void setUpAll() {
    final var flyway = Flyway.configure()
        .dataSource(mySqlContainer.getJdbcUrl(), mySqlContainer.getUsername(), mySqlContainer.getPassword())
        .load();
    flyway.migrate();
  }

  @Test
  void shouldCreateTask(final Statistics statistics) {
    // given
    final var task = setUpTask();

    clear(statistics);

    // when
    final var actual = taskDataAccess.saveAndFlush(task);

    // then
    assertThat(actual).extracting("id").isNotNull();

    assertThat(statistics)
        .hasEntityInsertCount(1)
        .hasEntityLoadCount(0)
        .hasEntityFetchCount(0)
        .hasEntityUpdateCount(0)
        .hasEntityDeleteCount(0);
  }

  @Test
  void shouldCreateTaskWithTag(final Statistics statistics) {
    // given
    final var tag = setUpTag();
    final var task = setUpTask().addTag(tag);

    clear(statistics);

    // when
    final var actual = taskDataAccess.saveAndFlush(task);

    // then
    assertThat(actual.getId()).isNotNull();
    assertThat(actual.getTags()).extracting("id").isNotNull();

    assertThat(statistics)
        .hasEntityInsertCount(2)
        .hasEntityLoadCount(0)
        .hasEntityFetchCount(0)
        .hasEntityUpdateCount(0)
        .hasEntityDeleteCount(0);
  }

  @Test
  void shouldRetrieveTaskByUuid(final Statistics statistics) {
    // given
    final var tagName = faker.lorem().word();
    final var tagDescription = faker.lorem().sentence();
    final var tag = setUpTag(tagName, tagDescription);

    final var taskTitle = faker.lorem().word();
    final var taskDescription = faker.lorem().sentence();
    final var task = setUpTask(taskTitle, taskDescription);
    task.addTag(tag);

    taskDataAccess.saveAndFlush(task);

    clear(statistics);

    // when
    final var actual = taskDataAccess.findByUuid(task.getId().value());

    // then
    TaskAssert.assertThat(actual.get())
        .hasTitle(TaskTitle.from(taskTitle))
        .hasDescription(Optional.of(TaskDescription.from(taskDescription)));
//    assertThat(actual.get().getTags())
//        .extracting("name", "description")
//        .contains(tuple(tagName, tagDescription));

    assertThat(statistics)
        .hasEntityInsertCount(0)
        .hasEntityLoadCount(2)
        .hasEntityFetchCount(0)
        .hasEntityUpdateCount(0)
        .hasEntityDeleteCount(0);
  }

  @Test
  void shouldRetrieveAllTasks(final Statistics statistics) {
    // given
    taskDataAccess.saveAndFlush(setUpTask());
    taskDataAccess.saveAndFlush(setUpTask());

    clear(statistics);

    // when
    final var actual = taskDataAccess.findAll();

    // then
    assertThat(actual).hasSize(2);

    assertThat(statistics)
        .hasEntityInsertCount(0)
        .hasEntityLoadCount(2)
        .hasEntityFetchCount(0)
        .hasEntityUpdateCount(0)
        .hasEntityDeleteCount(0);

  }

  @Test
  void shouldDeleteTaskByUuid(final Statistics statistics) {
    // given
    final var tag = setUpTag();
    final var task = setUpTask().addTag(tag);

    taskDataAccess.saveAndFlush(task);

    clear(statistics);

    // when
    taskDataAccess.deleteByUuid(task.getId().value());
    final var actual = taskDataAccess.findByUuid(task.getId().value());

    // then
    assertThat(actual).isEmpty();

    assertThat(statistics)
        .hasEntityInsertCount(0)
        .hasEntityLoadCount(1)
        .hasEntityFetchCount(0)
        .hasEntityUpdateCount(0)
        .hasEntityDeleteCount(1);
  }

  private Task setUpTask() {
    final var title = faker.lorem().word();
    final var description = faker.lorem().sentence();
    return setUpTask(title, description);
  }

  private Task setUpTask(final String title, final String description) {
    return Task.create(TaskTitle.from(title), TaskDescription.from(description))
        .markAsFinished()
        .markAsArchived();
  }

  private Tag setUpTag() {
    final var name = faker.lorem().word();
    final var description = faker.lorem().sentence();

    return setUpTag(name, description);
  }

  private Tag setUpTag(final String tagName, final String description) {
    return Tag.create(TagName.from(tagName), TagDescription.from(description));
  }
}
