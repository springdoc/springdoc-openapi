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

package test.org.springdoc.api.v31.app1;

import java.net.URI;
import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@Tag(name = "people")
public class PeopleRestService {
	private final Map<String, PersonDTO> people = new ConcurrentHashMap<>();

	@GetMapping(value = "/people", produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(description = "List all people", responses = {
			@ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = PersonDTO.class))), responseCode = "200") })
	public Collection<PersonDTO> getPeople() {
		return people.values();
	}

	@Operation(description = "Find person by e-mail", responses = {
			@ApiResponse(content = @Content(schema = @Schema(implementation = PersonDTO.class)), responseCode = "200"),
			@ApiResponse(responseCode = "404", description = "Person with such e-mail doesn't exists") })
	@GetMapping(value = "/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
	public PersonDTO findPerson(
			@Parameter(description = "E-Mail address to lookup for", required = true) @PathVariable("email") final String email) {

		final PersonDTO person = people.get(email);

		if (person == null) {
			throw new RuntimeException("Person with such e-mail doesn't exists");
		}

		return person;
	}

	@PostMapping(value = "/{email}", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Operation(description = "Create new person", responses = {
			@ApiResponse(content = @Content(schema = @Schema(implementation = PersonDTO.class), mediaType = MediaType.APPLICATION_JSON_VALUE), headers = @Header(name = "Location"), responseCode = "201"),
			@ApiResponse(responseCode = "409", description = "Person with such e-mail already exists") })
	public ResponseEntity<String> addPerson(
			@Parameter(description = "E-Mail", required = true) @PathVariable("email") final String email,
			@Parameter(description = "First Name", required = true) @RequestParam("firstName") final String firstName,
			@Parameter(description = "Last Name", required = true) @RequestParam("lastName") final String lastName) {

		final PersonDTO person = people.get(email);

		if (person != null) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Person with such e-mail already exists");
		}

		people.put(email, new PersonDTO(email, firstName, lastName));
		final URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(UUID.randomUUID()).toUri();
		return ResponseEntity.created(location).build();
	}

	@DeleteMapping(value = "/{email}")
	@Operation(description = "Delete existing person", responses = {
			@ApiResponse(responseCode = "204", description = "Person has been deleted"),
			@ApiResponse(responseCode = "404", description = "Person with such e-mail doesn't exists") })
	public ResponseEntity<String> deletePerson(
			@Parameter(description = "E-Mail address to lookup for", required = true) @PathVariable("email") final String email) {
		if (people.remove(email) == null) {
			throw new RuntimeException("Person with such e-mail doesn't exists");
		}
		return ResponseEntity.noContent().build();
	}

	@PostMapping(path = "/api/test", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public void postSingleParameter(@RequestParam(name = "test_id") @NotNull final UUID testId) {
		System.out.println("Received test_id: " + testId);
	}
}
