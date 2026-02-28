/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  * Copyright 2019-2026 the original author or authors.
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
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springdoc.core.utils.SchemaUtils;

import org.springframework.core.MethodParameter;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link MethodParameterPojoExtractor}.
 */
class MethodParameterPojoExtractorTest {

	private MethodParameterPojoExtractor methodParameterPojoExtractor = new MethodParameterPojoExtractor(new SchemaUtils(Optional.empty()));

	/**
	 * Tests for {@link MethodParameterPojoExtractor#extractFrom(Class<?>)}.
	 */
	@Nested
	class extractFrom {
		/**
		 * If record object should get field.
		 */
		@Test
		void ifRecordObjectShouldGetField() {
			Stream<MethodParameter> actual = methodParameterPojoExtractor.extractFrom(RecordObject.class);
			assertThat(actual)
					.extracting(MethodParameter::getMethod)
					.extracting(Method::getName)
					.containsOnlyOnce("email", "firstName", "lastName");
		}

		/**
		 * If class object should get method.
		 */
		@Test
		void ifClassObjectShouldGetMethod() {
			Stream<MethodParameter> actual = methodParameterPojoExtractor.extractFrom(ClassObject.class);
			assertThat(actual)
					.extracting(MethodParameter::getMethod)
					.extracting(Method::getName)
					.containsOnlyOnce("getEmail", "getFirstName", "getLastName");
		}

		/**
		 * The type Record object.
		 */
		public record RecordObject(String email, String firstName, String lastName) {}

		/**
		 * The type Class object.
		 */
		public class ClassObject {
			/**
			 * The Email.
			 */
			private String email;

			/**
			 * The First name.
			 */
			private String firstName;

			/**
			 * The Last name.
			 */
			private String lastName;

			/**
			 * Instantiates a new Class object.
			 *
			 * @param email     the email
			 * @param firstName the first name
			 * @param lastName  the last name
			 */
			public ClassObject(String email, String firstName, String lastName) {
				this.email = email;
				this.firstName = firstName;
				this.lastName = lastName;
			}

			/**
			 * Gets email.
			 *
			 * @return the email
			 */
			public String getEmail() {
				return email;
			}

			/**
			 * Gets first name.
			 *
			 * @return the first name
			 */
			public String getFirstName() {
				return firstName;
			}

			/**
			 * Gets last name.
			 *
			 * @return the last name
			 */
			public String getLastName() {
				return lastName;
			}
		}
	}
}
