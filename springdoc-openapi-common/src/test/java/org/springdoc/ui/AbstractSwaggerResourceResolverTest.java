package org.springdoc.ui;

import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springdoc.core.SwaggerUiConfigProperties;

import static org.junit.jupiter.api.Assertions.*;

class AbstractSwaggerResourceResolverTest {
	private SwaggerUiConfigProperties swaggerUiConfigProperties;

	private AbstractSwaggerResourceResolver abstractSwaggerResourceResolver;

	private final String VERSION = "4.18.2";

	@BeforeEach
	public void setup(){
		swaggerUiConfigProperties = new SwaggerUiConfigProperties();
		swaggerUiConfigProperties.setVersion(VERSION);
		abstractSwaggerResourceResolver = new AbstractSwaggerResourceResolver(swaggerUiConfigProperties);
	}

	@Test
	void findWebJarResourcePath() {
		String path = "swagger-ui/swagger-initializer.js";

		String actual = abstractSwaggerResourceResolver.findWebJarResourcePath(path);
		assertEquals("swagger-ui/4.18.2/swagger-initializer.js", actual);
	}

	@Test
	void returNullWhenPathIsSameAsWebjar() {
		String path = "swagger-ui";

		String actual = abstractSwaggerResourceResolver.findWebJarResourcePath(path);
		assertTrue(Objects.isNull(actual));
	}

	@Test
	void returNullWhenVersionIsNull() {
		String path = "swagger-ui/swagger-initializer.js";
		swaggerUiConfigProperties.setVersion(null);

		String actual = abstractSwaggerResourceResolver.findWebJarResourcePath(path);
		assertTrue(Objects.isNull(actual));
	}
}
