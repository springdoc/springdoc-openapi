package org.springdoc.ui;

import java.nio.file.Path;
import java.util.Objects;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springdoc.core.properties.SwaggerUiConfigProperties;

class AbstractSwaggerResourceResolverTest {
  private SwaggerUiConfigProperties swaggerUiConfigProperties;

  private AbstractSwaggerResourceResolver abstractSwaggerResourceResolver;

  private final String VERSION = "4.18.2";

  @BeforeEach
  public void setup() {
    swaggerUiConfigProperties = new SwaggerUiConfigProperties();
    swaggerUiConfigProperties.setVersion(VERSION);
    abstractSwaggerResourceResolver =
        new AbstractSwaggerResourceResolver(swaggerUiConfigProperties);
  }

  @Test
  void findWebJarResourcePath() {
    final String path = "swagger-ui/swagger-initializer.js";

    final String actual = abstractSwaggerResourceResolver.findWebJarResourcePath(path);
    final Path actuaPath = Path.of(actual, "");
    final Path expected = Path.of("swagger-ui/4.18.2/swagger-initializer.js", "");
    Assertions.assertEquals(expected, actuaPath);
  }

  @Test
  void returNullWhenPathIsSameAsWebjar() {
    final String path = "swagger-ui";

    final String actual = abstractSwaggerResourceResolver.findWebJarResourcePath(path);
    Assertions.assertTrue(Objects.isNull(actual));
  }

  @Test
  void returNullWhenVersionIsNull() {
    final String path = "swagger-ui/swagger-initializer.js";
    swaggerUiConfigProperties.setVersion(null);

    final String actual = abstractSwaggerResourceResolver.findWebJarResourcePath(path);
    Assertions.assertTrue(Objects.isNull(actual));
  }
}
