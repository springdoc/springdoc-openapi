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

package test.org.springdoc.ai.mcp.exclude;

import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Test controller with 5 endpoints across included and excluded paths. Used to verify
 * {@code springdoc.ai.mcp.paths-to-exclude} filtering.
 *
 * @author bnasslahsen
 */
@RestController
public class ProductController {

	/**
	 * List all products.
	 * @param category the optional category filter
	 * @return the product list
	 */
	@GetMapping("/products")
	@Operation(summary = "List all products", operationId = "listProducts")
	public List<Map<String, String>> listProducts(
			@Parameter(description = "Filter by category") @RequestParam(required = false) String category) {
		return List.of(Map.of("id", "1", "name", "Widget", "category", "hardware"));
	}

	/**
	 * Get a product by ID.
	 * @param id the product id
	 * @return the product
	 */
	@GetMapping("/products/{id}")
	@Operation(summary = "Get a product by ID", operationId = "getProductById")
	public Map<String, String> getProductById(
			@Parameter(description = "The product ID") @PathVariable String id) {
		return Map.of("id", id, "name", "Widget");
	}

	/**
	 * List admin users (excluded).
	 * @return the admin user list
	 */
	@GetMapping("/admin/users")
	@Operation(summary = "List admin users", operationId = "listAdminUsers")
	public List<Map<String, String>> listAdminUsers() {
		return List.of(Map.of("id", "1", "name", "Admin"));
	}

	/**
	 * Delete an admin user (excluded).
	 * @param id the user id
	 * @return the deleted user
	 */
	@DeleteMapping("/admin/users/{id}")
	@Operation(summary = "Delete an admin user", operationId = "deleteAdminUser")
	public Map<String, String> deleteAdminUser(@PathVariable String id) {
		return Map.of("id", id, "deleted", "true");
	}

	/**
	 * Get internal health status (excluded).
	 * @return the health status
	 */
	@GetMapping("/internal/health")
	@Operation(summary = "Get internal health status", operationId = "getHealthStatus")
	public Map<String, String> getHealthStatus() {
		return Map.of("status", "UP");
	}

}
