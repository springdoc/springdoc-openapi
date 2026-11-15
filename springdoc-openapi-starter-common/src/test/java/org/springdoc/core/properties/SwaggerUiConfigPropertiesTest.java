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

package org.springdoc.core.properties;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the resolution of the swagger-ui webjar version served by springdoc-openapi.
 *
 * @author bnasslahsen
 */
class SwaggerUiConfigPropertiesTest {

	private static final String BUILT_VERSION = "5.34.0";

	@TempDir
	Path classpath;

	@Test
	void keepsBuiltVersionWhenItsResourcesArePresent() throws IOException {
		addWebJar(BUILT_VERSION);
		addWebJarProperties("5.35.0");
		assertThat(resolve()).isEqualTo(BUILT_VERSION);
	}

	@Test
	void usesOverriddenWebJarVersion() throws IOException {
		addWebJar("5.35.0");
		addWebJarProperties("5.35.0");
		assertThat(resolve()).isEqualTo("5.35.0");
	}

	@Test
	void keepsBuiltVersionWithoutWebJarProperties() throws IOException {
		addWebJar("5.35.0");
		assertThat(resolve()).isEqualTo(BUILT_VERSION);
	}

	@Test
	void keepsBuiltVersionWhenWebJarPropertiesDoNotMatchItsResources() throws IOException {
		addWebJar("5.35.0");
		addWebJarProperties("5.36.0");
		assertThat(resolve()).isEqualTo(BUILT_VERSION);
	}

	private String resolve() throws IOException {
		try (URLClassLoader classLoader = new URLClassLoader(new URL[] { classpath.toUri().toURL() }, null)) {
			return SwaggerUiConfigProperties.resolveWebJarVersion(BUILT_VERSION, classLoader);
		}
	}

	private void addWebJar(String version) throws IOException {
		Path index = classpath.resolve("META-INF/resources/webjars/swagger-ui/" + version + "/index.html");
		Files.createDirectories(index.getParent());
		Files.writeString(index, "<html></html>");
	}

	private void addWebJarProperties(String version) throws IOException {
		Path properties = classpath.resolve(SwaggerUiConfigProperties.SWAGGER_UI_WEBJAR_POM_PROPERTIES);
		Files.createDirectories(properties.getParent());
		Files.writeString(properties, "version=" + version + "\ngroupId=org.webjars\nartifactId=swagger-ui\n");
	}

}
