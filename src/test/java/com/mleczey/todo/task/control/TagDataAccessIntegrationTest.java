package com.mleczey.todo.task.control;

import com.github.javafaker.Faker;
import com.mleczey.todo.DataAccessTest;
import com.mleczey.todo.task.entity.Tag;
import com.mleczey.todo.task.entity.TagDescription;
import com.mleczey.todo.task.entity.TagName;
import com.mleczey.todo.task.entity.Task;
import com.mleczey.todo.task.entity.TaskDescription;
import com.mleczey.todo.task.entity.TaskTitle;
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

class TagDataAccessIntegrationTest extends DataAccessTest {

  @Container
  static MySQLContainer mySqlContainer = new MySQLContainer("mysql:8.0.21")
      .withDatabaseName("todo")
      .withUsername("admin")
      .withPassword("admin");

  @Autowired
  private TagDataAccess tagDataAccess;

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
  void shouldCreateTag(final Statistics statistics) {
    // given
    final var tag = setUpTag();

    clear(statistics);

    // when
    final var actual = tagDataAccess.saveAndFlush(tag);

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
  void shouldCreateLinkBetweenTaskAndTag(final Statistics statistics) {
    // given
    final var tagName = faker.lorem().word();
    final var tagDescription = faker.lorem().sentence();
    final var tag = tagDataAccess.saveAndFlush(setUpTag(tagName, tagDescription));

    final var taskTitle = faker.lorem().word();
    final var taskDescription = faker.lorem().sentence();
    final var task = taskDataAccess.saveAndFlush(setUpTask(taskTitle, taskDescription));
    task.addTag(tag);

    clear(statistics);

    // when
    final var actual = taskDataAccess.findByUuid(task.getId().value());

    // then
    assertThat(actual).get().extracting("title", "description")
        .contains(taskTitle, taskDescription);
//    assertThat(actual.map(EntitiesHelper::getTags)).get()
//        .extracting(tags -> tags.iterator().next())
//        .extracting("name", "description")
//        .contains(tagName, tagDescription);

    assertThat(statistics)
        .hasEntityInsertCount(0)
        .hasEntityLoadCount(2)
        .hasEntityFetchCount(0)
        .hasEntityUpdateCount(0)
        .hasEntityDeleteCount(0);
  }

  @Test
  void shouldRetrieveTagByName(final Statistics statistics) {
    // given
    final var name = faker.lorem().word();
    final var expected = tagDataAccess.saveAndFlush(setUpTag(name, faker.lorem().sentence()));

    clear(statistics);

    // when
    final var actual = tagDataAccess.findByName(name);

    // then
    assertThat(actual).get().extracting(Tag::getId)
        .isEqualTo(expected.getId());

    assertThat(statistics)
        .hasEntityInsertCount(0)
        .hasEntityLoadCount(1)
        .hasEntityFetchCount(0)
        .hasEntityUpdateCount(0)
        .hasEntityDeleteCount(0);
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
