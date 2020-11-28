package com.mleczey.todo;

import java.lang.reflect.Method;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.platform.commons.util.ClassUtils;

public class DisplayNameCamelCase extends DisplayNameGenerator.Standard {

  @Override
  public String generateDisplayNameForMethod(Class<?> testClass, Method testMethod) {
    return insertSpacesBetweenWords(testMethod.getName()) + '(' + ClassUtils.nullSafeToString(Class::getSimpleName, testMethod.getParameterTypes()) + ").";
  }

  @Override
  public String generateDisplayNameForNestedClass(Class<?> nestedClass) {
    return insertSpacesBetweenWords(super.generateDisplayNameForNestedClass(nestedClass)) + "...";
  }

  @Override
  public String generateDisplayNameForClass(Class<?> testClass) {
    return insertSpacesBetweenWords(super.generateDisplayNameForClass(testClass));
  }

  private String insertSpacesBetweenWords(final String s) {
    var result = new StringBuilder();
    result.append(s.charAt(0));
    for (int i = 1; i < s.length(); i++) {
      if (Character.isUpperCase(s.charAt(i))) {
        result.append(' ');
        result.append(Character.toLowerCase(s.charAt(i)));
      } else {
        result.append(s.charAt(i));
      }
    }
    return result.toString();
  }
}
