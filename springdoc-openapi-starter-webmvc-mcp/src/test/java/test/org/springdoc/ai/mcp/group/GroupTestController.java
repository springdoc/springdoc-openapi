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

package test.org.springdoc.ai.mcp.group;

import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Test controllers with different @Tag annotations for group testing.
 *
 * @author bnasslahsen
 */
public class GroupTestController {

	/**
	 * Users controller tagged with "Users".
	 *
	 * @author bnasslahsen
	 */
	@RestController
	@Tag(name = "Users")
	public static class UserController {

		/**
		 * List all users.
		 * @return the user list
		 */
		@GetMapping("/users")
		@Operation(summary = "List all users", operationId = "listUsers")
		public List<Map<String, String>> listUsers() {
			return List.of(Map.of("id", "1", "name", "Alice"));
		}

		/**
		 * Get a user by ID.
		 * @param id the user id
		 * @return the user
		 */
		@GetMapping("/users/{id}")
		@Operation(summary = "Get a user by ID", operationId = "getUserById")
		public Map<String, String> getUserById(@PathVariable String id) {
			return Map.of("id", id, "name", "Alice");
		}

	}

	/**
	 * Products controller tagged with "Products".
	 *
	 * @author bnasslahsen
	 */
	@RestController
	@Tag(name = "Products")
	public static class ProductController {

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
		 * Create a product.
		 * @param product the product data
		 * @return the created product
		 */
		@PostMapping("/products")
		@Operation(summary = "Create a product", operationId = "createProduct")
		public Map<String, String> createProduct(@RequestBody Map<String, String> product) {
			return product;
		}

	}

}
