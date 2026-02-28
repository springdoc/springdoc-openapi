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

package test.org.springdoc.api.v31.app58;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

	@PostMapping("/examplePost")
	@Operation(summary = "schema example")
	public Object example(@Parameter(schema = @Schema(hidden = true)) JsonNode json) {
		return null;
	}

	@GetMapping("/example")
	public void test(@Parameter(schema = @Schema(hidden = true)) JsonNode json) {
	}

	@GetMapping(value = "/foo")
	public void foobar(@Parameter(description = "User", name = "user",
			schema = @Schema(implementation = PersonDTO.class)) @RequestParam("bar") String bar) {

	}

	@GetMapping(value = "/foo1")
	public void foobar1(@Parameter(description = "User", name = "user",
			content = @Content(mediaType = "application/json",
					schema = @Schema(implementation = PersonDTO.class))) @RequestParam("bar") String bar) {

	}

	class PersonDTO {
		private String email;

		private String firstName;

		private String lastName;

		public PersonDTO() {
		}

		public PersonDTO(final String email, final String firstName, final String lastName) {
			this.email = email;
			this.firstName = firstName;
			this.lastName = lastName;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(final String email) {
			this.email = email;
		}

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(final String firstName) {
			this.firstName = firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public void setLastName(final String lastName) {
			this.lastName = lastName;
		}
	}

}
