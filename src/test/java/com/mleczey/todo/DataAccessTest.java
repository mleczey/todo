package com.mleczey.todo;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Testcontainers;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
@ExtendWith(value = {SpringExtension.class, StatisticsResolver.class})
@Testcontainers
public abstract class DataAccessTest {

  @Autowired
  private EntityManagerFactory emf;

  @Autowired
  private EntityManager em;

  protected void clear(final Statistics statistics) {
    em.flush();
    em.clear();
    statistics.clear();
  }
}
