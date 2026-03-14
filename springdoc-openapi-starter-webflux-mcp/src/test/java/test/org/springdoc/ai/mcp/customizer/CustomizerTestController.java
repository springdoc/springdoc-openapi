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

package test.org.springdoc.ai.mcp.customizer;

import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Test controller for McpToolCustomizer tests in a reactive context.
 *
 * @author bnasslahsen
 */
@RestController
public class CustomizerTestController {

	/**
	 * List all users.
	 * @return the user list
	 */
	@GetMapping("/users")
	@Operation(summary = "List all users", operationId = "listUsers")
	public Flux<Map<String, String>> listUsers() {
		return Flux.just(Map.of("id", "1", "name", "John"));
	}

	/**
	 * Get a user by ID.
	 * @param id the user id
	 * @return the user
	 */
	@GetMapping("/users/{id}")
	@Operation(summary = "Get a user by ID", operationId = "getUserById")
	public Mono<Map<String, String>> getUserById(@PathVariable String id) {
		return Mono.just(Map.of("id", id, "name", "John"));
	}

	/**
	 * Create a new user.
	 * @param user the user data
	 * @return the created user
	 */
	@PostMapping("/users")
	@Operation(summary = "Create a new user", operationId = "createUser")
	public Mono<Map<String, String>> createUser(@RequestBody Map<String, String> user) {
		return Mono.just(user);
	}

}
