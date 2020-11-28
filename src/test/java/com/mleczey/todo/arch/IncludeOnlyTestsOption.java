package com.mleczey.todo.arch;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.core.importer.Location;
import java.util.regex.Pattern;

final class IncludeOnlyTestsOption implements ImportOption {

  private static final Pattern MAVEN_PATTERN = Pattern.compile(".*/target/test-classes/.*");

  @Override
  public boolean includes(Location location) {
    return location.matches(MAVEN_PATTERN);
  }

}
