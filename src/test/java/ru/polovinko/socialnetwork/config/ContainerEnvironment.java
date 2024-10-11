package ru.polovinko.socialnetwork.config;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.polovinko.socialnetwork.container.PostgresTestContainer;

@Testcontainers
public class ContainerEnvironment {
  @Container
  public static PostgreSQLContainer postgreSQLContainer = PostgresTestContainer.getInstance();
}
