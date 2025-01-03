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

/**
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech) (3.0.0).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */
package test.org.springdoc.api.v31.app105.api.user;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import test.org.springdoc.api.v31.app105.model.User;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@jakarta.annotation.Generated(value = "org.springdoc.demo.app2.codegen.languages.SpringCodegen", date = "2019-07-11T00:09:29.839+02:00[Europe/Paris]")

@Tag(name = "user", description = "the user API")
public interface UserApi {

	default UserApiDelegate getDelegate() {
		return new UserApiDelegate() {
		};
	}

	@Operation(summary = "Create user", tags = { "user" })
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successful operation") })
	@PostMapping(value = "/user", consumes = { "application/json" })
	default ResponseEntity<Void> createUser(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Created user object", required = true) @Valid @RequestBody User user) {
		return getDelegate().createUser(user);
	}

	@Operation(summary = "Creates list of users with given input array", tags = { "user" })
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successful operation") })

	@PostMapping(value = "/user/createWithArray", consumes = { "application/json" })
	default ResponseEntity<Void> createUsersWithArrayInput(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "List of user object", required = true) @Valid @RequestBody List<User> user) {
		return getDelegate().createUsersWithArrayInput(user);
	}

	@Operation(summary = "Creates list of users with given input array", tags = { "user" })
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successful operation") })
	@PostMapping(value = "/user/createWithList", consumes = { "application/json" })
	default ResponseEntity<Void> createUsersWithListInput(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "List of user object", required = true) @Valid @RequestBody List<User> user) {
		return getDelegate().createUsersWithListInput(user);
	}

	@Operation(summary = "Creates list of users with given input array", tags = { "user" })
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successful operation") })

	@DeleteMapping(value = "/user/{username}")
	default ResponseEntity<Void> deleteUser(
			@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The name that needs to be deleted", required = true) @PathVariable("username") String username) {
		return getDelegate().deleteUser(username);
	}

	@Operation(summary = "Get user by user name", tags = { "user" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = User.class))),
			@ApiResponse(responseCode = "400", description = "Invalid username supplied"),
			@ApiResponse(responseCode = "404", description = "User not found") })

	@GetMapping(value = "/user/{username}", produces = { "application/xml", "application/json" })
	default ResponseEntity<User> getUserByName(
			@Parameter(description = "The name that needs to be fetched. Use user1 for testing.", required = true) @PathVariable("username") String username) {
		return getDelegate().getUserByName(username);
	}

	@Operation(summary = "Logs user into the system", tags = { "user" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "400", description = "Invalid username/password supplied") })
	@GetMapping(value = "/user/login", produces = { "application/xml", "application/json" })
	default ResponseEntity<String> loginUser(
			@NotNull @Parameter(description = "The user name for login", required = true) @Valid @RequestParam(value = "username", required = true) String username,
			@NotNull @Parameter(description = "The password for login in clear text", required = true) @Valid @RequestParam(value = "password", required = true) String password) {
		return getDelegate().loginUser(username, password);
	}

	@Operation(summary = "Logs out current logged in user session", tags = { "user" })
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successful operation") })
	@GetMapping(value = "/user/logout")
	default ResponseEntity<Void> logoutUser() {
		return getDelegate().logoutUser();
	}

	@Operation(summary = "Updated user", tags = { "user" })
	@ApiResponses(value = { @ApiResponse(responseCode = "400", description = "Invalid user supplied"),
			@ApiResponse(responseCode = "404", description = "User not found") })
	@PutMapping(value = "/user/{username}", consumes = { "application/json" })
	default ResponseEntity<Void> updateUser(
			@Parameter(description = "name that need to be deleted", required = true) @PathVariable("username") String username,
			@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated user object", required = true) @Valid @RequestBody User user) {
		return getDelegate().updateUser(username, user);
	}

}