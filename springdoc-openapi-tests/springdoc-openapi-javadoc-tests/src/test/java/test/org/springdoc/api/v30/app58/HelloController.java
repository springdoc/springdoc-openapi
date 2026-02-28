/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2026 the original author or authors.
 *  *  *  *  *  *
 *  *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *  *
 *  *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *  *
 *  *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  *  * limitations under the License.
 *  *  *  *  *
 *  *  *  *
 *  *  *
 *  *
 *
 */

package test.org.springdoc.api.v30.app58;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The type Hello controller.
 */
@RestController
class HelloController {

	/**
	 * Example object.
	 *
	 * @param json the json
	 * @return the object
	 */
	@PostMapping("/examplePost")
	@Operation(summary = "schema example")
	public Object example(@Parameter(schema = @Schema(hidden = true)) JsonNode json) {
		return null;
	}

	/**
	 * Test.
	 *
	 * @param json the json
	 */
	@GetMapping("/example")
	public void test(@Parameter(schema = @Schema(hidden = true)) JsonNode json) {
	}

	/**
	 * Foobar.
	 *
	 * @param bar the bar
	 */
	@GetMapping(value = "/foo")
	public void foobar(@Parameter(description = "User", name = "user",
			schema = @Schema(implementation = PersonDTO.class)) @RequestParam("bar") String bar) {

	}

	/**
	 * Foobar 1.
	 *
	 * @param bar the bar
	 */
	@GetMapping(value = "/foo1")
	public void foobar1(@Parameter(description = "User", name = "user",
			content = @Content(mediaType = "application/json",
					schema = @Schema(implementation = PersonDTO.class))) @RequestParam("bar") String bar) {

	}

	/**
	 * The type Person dto.
	 */
	class PersonDTO {
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
		 * Instantiates a new Person dto.
		 */
		public PersonDTO() {
		}

		/**
		 * Instantiates a new Person dto.
		 *
		 * @param email     the email
		 * @param firstName the first name
		 * @param lastName  the last name
		 */
		public PersonDTO(final String email, final String firstName, final String lastName) {
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
		 * Sets email.
		 *
		 * @param email the email
		 */
		public void setEmail(final String email) {
			this.email = email;
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
		 * Sets first name.
		 *
		 * @param firstName the first name
		 */
		public void setFirstName(final String firstName) {
			this.firstName = firstName;
		}

		/**
		 * Gets last name.
		 *
		 * @return the last name
		 */
		public String getLastName() {
			return lastName;
		}

		/**
		 * Sets last name.
		 *
		 * @param lastName the last name
		 */
		public void setLastName(final String lastName) {
			this.lastName = lastName;
		}
	}

}
