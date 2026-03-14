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

package test.org.springdoc.ai.mcp.filter;

import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Test controller with various endpoints for filter testing.
 *
 * @author bnasslahsen
 */
@RestController
public class FilterTestController {

	/**
	 * List all products.
	 * @return the product list
	 */
	@GetMapping("/products")
	@Operation(summary = "List all products", operationId = "listProducts")
	public List<Map<String, String>> listProducts() {
		return List.of(Map.of("id", "1", "name", "Widget"));
	}

	/**
	 * Get a product by ID.
	 * @param id the product id
	 * @return the product
	 */
	@GetMapping("/products/{id}")
	@Operation(summary = "Get a product by ID", operationId = "getProductById")
	public Map<String, String> getProductById(@PathVariable String id) {
		return Map.of("id", id, "name", "Widget");
	}

	/**
	 * List admin users.
	 * @return the admin user list
	 */
	@GetMapping("/admin/users")
	@Operation(summary = "List admin users", operationId = "listAdminUsers")
	public List<Map<String, String>> listAdminUsers() {
		return List.of(Map.of("id", "1", "name", "Admin"));
	}

	/**
	 * Delete an admin user.
	 * @param id the user id
	 * @return the result
	 */
	@DeleteMapping("/admin/users/{id}")
	@Operation(summary = "Delete an admin user", operationId = "deleteAdminUser")
	public Map<String, String> deleteAdminUser(@PathVariable String id) {
		return Map.of("deleted", id);
	}

	/**
	 * Internal health check.
	 * @return the health status
	 */
	@GetMapping("/internal/health")
	@Operation(summary = "Internal health check", operationId = "internalHealth")
	public Map<String, String> internalHealth() {
		return Map.of("status", "UP");
	}

}
