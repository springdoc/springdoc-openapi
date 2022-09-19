/*
 *
 *  *
 *  *  *
 *  *  *  * Copyright 2019-2022 the original author or authors.
 *  *  *  *
 *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  * You may obtain a copy of the License at
 *  *  *  *
 *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *
 *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  * See the License for the specific language governing permissions and
 *  *  *  * limitations under the License.
 *  *  *
 *  *
 *
 */

package org.springdoc.core;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.io.TempDir;

import org.springframework.core.MethodParameter;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link MethodParameterPojoExtractor}.
 */
class MethodParameterPojoExtractorTest {
	@TempDir
	File tempDir;

	/**
	 * Tests for {@link MethodParameterPojoExtractor#extractFrom(Class<?>)}.
	 */
	@Nested
	class extractFrom {
		@Test
		@EnabledForJreRange(min = JRE.JAVA_17)
		void ifRecordObjectShouldGetField() throws IOException, ClassNotFoundException {
			File recordObject = new File(tempDir, "RecordObject.java");
			try (PrintWriter writer = new PrintWriter(new FileWriter(recordObject))) {
				writer.println("public record RecordObject(String email, String firstName, String lastName){");
				writer.println("}");
			}
			String[] args = {
					recordObject.getAbsolutePath()
			};
			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			int r = compiler.run(null, null, null, args);
			if (r != 0) {
				throw new IllegalStateException("Compilation failed");
			}
			URL[] urls = { tempDir.toURI().toURL() };
			ClassLoader loader = URLClassLoader.newInstance(urls);

			Class<?> clazz = loader.loadClass("RecordObject");

			Stream<MethodParameter> actual = MethodParameterPojoExtractor.extractFrom(clazz);
			assertThat(actual)
					.extracting(MethodParameter::getMethod)
					.extracting(Method::getName)
					.containsOnlyOnce("email", "firstName", "lastName");
		}

		@Test
		void ifClassObjectShouldGetMethod() {
			Stream<MethodParameter> actual = MethodParameterPojoExtractor.extractFrom(ClassObject.class);
			assertThat(actual)
					.extracting(MethodParameter::getMethod)
					.extracting(Method::getName)
					.containsOnlyOnce("getEmail", "getFirstName", "getLastName");
		}

		public class ClassObject {
			private String email;

			private String firstName;

			private String lastName;

			public ClassObject(String email, String firstName, String lastName) {
				this.email = email;
				this.firstName = firstName;
				this.lastName = lastName;
			}

			public String getEmail() {
				return email;
			}

			public String getFirstName() {
				return firstName;
			}

			public String getLastName() {
				return lastName;
			}
		}
	}
}
