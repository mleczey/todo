package com.mleczey.todo;

import java.util.Objects;
import org.assertj.core.api.AbstractAssert;
import org.hibernate.stat.Statistics;

public class StatisticsAssert extends AbstractAssert<StatisticsAssert, Statistics> {

  private StatisticsAssert(Statistics actual) {
    super(actual, StatisticsAssert.class);
  }

  public StatisticsAssert hasEntityInsertCount(final long expected) {
    long count = actual.getEntityInsertCount();
    if (!Objects.equals(expected, count)) {
      failWithMessage("Expected entity insert count to be <%d> but was <%d>.", expected, count);
    }

    return this;
  }

  public StatisticsAssert hasEntityLoadCount(final long expected) {
    long count = actual.getEntityLoadCount();
    if (!Objects.equals(expected, count)) {
      failWithMessage("Expected entity load count to be <%d> but was <%d>.", expected, count);
    }

    return this;
  }

  public StatisticsAssert hasEntityFetchCount(final long expected) {
    long count = actual.getEntityFetchCount();
    if (!Objects.equals(expected, count)) {
      failWithMessage("Expected entity fetch count to be <%d> but was <%d>.", expected, count);
    }

    return this;
  }

  public StatisticsAssert hasEntityUpdateCount(final long expected) {
    long count = actual.getEntityUpdateCount();
    if (!Objects.equals(expected, count)) {
      failWithMessage("Expected entity update count to be <%d> but was <%d>.", expected, count);
    }

    return this;
  }

  public StatisticsAssert hasEntityDeleteCount(final long expected) {
    long count = actual.getEntityDeleteCount();
    if (!Objects.equals(expected, count)) {
      failWithMessage("Expected entity delete count to be <%d> but was <%d>.", expected, count);
    }

    return this;
  }

  public StatisticsAssert hasQueryCount(final long expected) {
    long count = actual.getQueryExecutionCount();
    if (!Objects.equals(expected, count)) {
      failWithMessage("Expected query count to be <%d> but was <%d>.", expected, count);
    }

    return this;
  }

  public static StatisticsAssert assertThat(final Statistics statistics) {
    return new StatisticsAssert(statistics);
  }
}
