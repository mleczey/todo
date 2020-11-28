package com.mleczey.todo.configuration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.junit.jupiter.api.Test;

class UpperUnderscoreNamingStrategyTest {

  private final JdbcEnvironment NOT_RELEVANT = null;

  final UpperUnderscoreNamingStrategy testedObject = new UpperUnderscoreNamingStrategy();
  
  @Test
  void shouldChangeTableNameToUpperUnderscoreTableName() {
    // given
    final var identifier = Identifier.toIdentifier("TableName");

    // when
    final var actual = testedObject.toPhysicalTableName(identifier, NOT_RELEVANT);

    // then
    assertThat(actual.getText()).isEqualTo("TABLE_NAME");
  }

  @Test
  void shouldNotChangeTableNameWhenUpperUnderscoreName() {
    // given
    final var identifier = Identifier.toIdentifier("TABLE_NAME");

    // when
    final var actual = testedObject.toPhysicalTableName(identifier, NOT_RELEVANT);

    // then
    assertThat(actual.getText()).isEqualTo("TABLE_NAME");
  }

  @Test
  void shouldChangeColumnNameToUpperUnderscoreColumnName() {
    // given
    final var identifier = Identifier.toIdentifier("columnName");

    // when
    final var actual = testedObject.toPhysicalColumnName(identifier, NOT_RELEVANT);

    // then
    assertThat(actual.getText()).isEqualTo("COLUMN_NAME");
  }

  @Test
  void shouldNotChangeColumnNameWhenUpperUnderscoreName() {
    // given
    final var identifier = Identifier.toIdentifier("COLUMN_NAME");

    // when
    final var actual = testedObject.toPhysicalColumnName(identifier, NOT_RELEVANT);

    // then
    assertThat(actual.getText()).isEqualTo("COLUMN_NAME");
  }
}
