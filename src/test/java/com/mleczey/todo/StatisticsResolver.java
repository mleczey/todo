package com.mleczey.todo;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;
import javax.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatisticsResolver implements ParameterResolver {

  private static final Logger logger = LoggerFactory.getLogger(StatisticsResolver.class);

  @Override
  public boolean supportsParameter(final ParameterContext pc, final ExtensionContext ec) throws ParameterResolutionException {
    return pc.getParameter().getType().equals(Statistics.class);
  }

  @Override
  public Object resolveParameter(final ParameterContext pc, final ExtensionContext ec) throws ParameterResolutionException {
    return Stream.concat(ec.getTestClass().stream(), ec.getTestClass().map(Class::getSuperclass).stream())
        .map(Class::getDeclaredFields)
        .flatMap(Arrays::stream)
        .peek(field -> logger.debug("Checking field {}:{}", field.getName(), field.getType()))
        .filter(field -> field.getType().isAssignableFrom(EntityManagerFactory.class))
        .peek(field -> field.setAccessible(true))
        .findFirst()
        .flatMap(field -> ec.getTestInstance().flatMap(o -> map(field, o)))
        .map(EntityManagerFactory.class::cast)
        .map(StatisticsResolver::toStatistics)
        .orElseThrow(() -> new IllegalStateException("Statistics object could not be instantiated. Please check if you test class has " + EntityManagerFactory.class.getName() + "."));
  }

  private static Optional<Object> map(final Field field, final Object o) {
    Object result = null;
    try {
      result = field.get(o);
    } catch (final IllegalAccessException | IllegalArgumentException x) {
      logger.error("Error while getting field entity manager factory from test instance.", x);
    }
    return Optional.ofNullable(result);
  }

  private static Statistics toStatistics(final EntityManagerFactory emf) {
    final var statistics = emf.unwrap(SessionFactory.class).getStatistics();
    statistics.clear();
    return statistics;
  }
}
