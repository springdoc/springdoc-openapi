/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  * Copyright 2019-2024 the original author or authors.
 *  *  *  *  *
 *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *
 *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *
 *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  * limitations under the License.
 *  *  *  *
 *  *  *
 *  *
 *
 */
package org.springdoc.core.extractor;

import java.lang.reflect.Method;
import java.util.stream.Stream;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import org.springframework.core.MethodParameter;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link MethodParameterPojoExtractor}.
 */
class MethodParameterPojoExtractorTest {
	/**
	 * Tests for {@link MethodParameterPojoExtractor#extractFrom(Class<?>)}.
	 */
	@Nested
	class extractFrom {
		@Test
		void ifRecordObjectShouldGetField() {
			Stream<MethodParameter> actual = MethodParameterPojoExtractor.extractFrom(RecordObject.class);
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

		public record RecordObject(String email, String firstName, String lastName) {}

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
