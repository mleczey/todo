package com.mleczey.todo.configuration;

import com.google.common.base.CaseFormat;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

public class UpperUnderscoreNamingStrategy extends PhysicalNamingStrategyStandardImpl {

  private static final long serialVersionUID = 1L;

  @Override
  public Identifier toPhysicalTableName(final Identifier name, final JdbcEnvironment context) {
    return null == name
        ? super.toPhysicalTableName(name, context)
        : convert(name);
  }

  private static Identifier convert(final Identifier name) {
    final var text = name.getText();
    return areAllLettersUppercase(text)
        ? name
        : Identifier.toIdentifier(CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, name.getText()));
  }

  private static boolean areAllLettersUppercase(final String s) {
    final var replaced = s.replace("_", "");
    return StringUtils.isAllUpperCase(replaced);
  }

  @Override
  public Identifier toPhysicalColumnName(final Identifier name, final JdbcEnvironment context) {
    return null == name
        ? super.toPhysicalColumnName(name, context)
        : convert(name);
  }
}
