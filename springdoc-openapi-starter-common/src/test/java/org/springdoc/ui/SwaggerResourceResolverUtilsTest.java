/*
 * Copyright 2019-2026 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
