package org.springdoc.ui;

import java.io.File;
import java.util.Objects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springdoc.ui.SwaggerResourceResolverUtils.findSwaggerResourcePath;

class SwaggerResourceResolverUtilsTest {

	private final String VERSION = "4.18.2";

	@Test
	void findWebJarResourcePath() {
		String path = "swagger-ui/swagger-initializer.js";
		String actual = findSwaggerResourcePath(path, VERSION);
		assertEquals("swagger-ui" + File.separator + "4.18.2" + File.separator + "swagger-initializer.js", actual);
	}

	@Test
	void returnNullWhenPathIsSameAsWebjar() {
		String path = "swagger-ui";
		String actual = findSwaggerResourcePath(path, VERSION);
		assertTrue(Objects.isNull(actual));
	}

	@Test
	void returnNullWhenVersionIsNull() {
		String path = "swagger-ui/swagger-initializer.js";
		String actual = findSwaggerResourcePath(path, null);
		assertTrue(Objects.isNull(actual));
	}
}
