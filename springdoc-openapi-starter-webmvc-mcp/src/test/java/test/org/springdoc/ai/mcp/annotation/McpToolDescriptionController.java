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

package test.org.springdoc.ai.mcp.annotation;

import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import org.springdoc.ai.annotations.McpToolDescription;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Test controller with {@link McpToolDescription} annotations.
 *
 * @author bnasslahsen
 */
@RestController
public class McpToolDescriptionController {

	/**
	 * List all orders.
	 * @return the order list
	 */
	@GetMapping("/orders")
	@Operation(summary = "List all orders", operationId = "listOrders")
	@McpToolDescription("Fetch every order in the system including pending and completed ones")
	public List<Map<String, String>> listOrders() {
		return List.of(Map.of("id", "1", "status", "pending"));
	}

	/**
	 * Get an order by ID.
	 * @param id the order id
	 * @return the order
	 */
	@GetMapping("/orders/{id}")
	@Operation(summary = "Get an order by ID", operationId = "getOrderById")
	@McpToolDescription(value = "Look up a single order by its unique identifier", name = "findOrder")
	public Map<String, String> getOrderById(@PathVariable String id) {
		return Map.of("id", id, "status", "pending");
	}

	/**
	 * Create a new order.
	 * @param order the order data
	 * @return the created order
	 */
	@PostMapping("/orders")
	@Operation(summary = "Create a new order", operationId = "createOrder")
	public Map<String, String> createOrder(@RequestBody Map<String, String> order) {
		return order;
	}

}
