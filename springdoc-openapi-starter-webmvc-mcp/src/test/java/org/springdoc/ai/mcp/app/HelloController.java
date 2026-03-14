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

package org.springdoc.ai.mcp.app;

import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Test controller simulating a typical REST API for MCP tool discovery.
 *
 * @author bnasslahsen
 */
@RestController
public class HelloController {

	/**
	 * Get a user by ID.
	 * @param id the user id
	 * @return the user map
	 */
	@GetMapping("/users/{id}")
	@Operation(summary = "Get a user by ID", operationId = "getUserById")
	public Map<String, String> getUserById(@Parameter(description = "The user ID") @PathVariable String id) {
		return Map.of("id", id, "name", "John Doe");
	}

	/**
	 * List all users with optional filtering.
	 * @param filter the optional filter
	 * @return the user list
	 */
	@GetMapping("/users")
	@Operation(summary = "List all users", operationId = "listUsers")
	public List<Map<String, String>> listUsers(
			@Parameter(description = "Filter criteria") @RequestParam(required = false) String filter) {
		return List.of(Map.of("id", "1", "name", "John Doe"));
	}

	/**
	 * Create a new user.
	 * @param user the user data
	 * @return the created user
	 */
	@PostMapping("/users")
	@Operation(summary = "Create a new user", operationId = "createUser")
	public Map<String, String> createUser(@RequestBody Map<String, String> user) {
		return user;
	}

}
