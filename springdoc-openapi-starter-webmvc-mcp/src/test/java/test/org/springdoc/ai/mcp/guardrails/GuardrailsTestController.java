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

package test.org.springdoc.ai.mcp.guardrails;

import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Test controller with both safe and mutating endpoints for guardrails override testing.
 *
 * @author bnasslahsen
 */
@RestController
public class GuardrailsTestController {

	/**
	 * List items (GET — safe by default).
	 * @return the item list
	 */
	@GetMapping("/items")
	@Operation(summary = "List all items", operationId = "listItems")
	public List<Map<String, String>> listItems() {
		return List.of(Map.of("id", "1", "name", "Item"));
	}

	/**
	 * Delete an item (DELETE — mutating by default).
	 * @param id the item id
	 * @return the result
	 */
	@DeleteMapping("/items/{id}")
	@Operation(summary = "Delete an item", operationId = "deleteItem")
	public Map<String, String> deleteItem(@PathVariable String id) {
		return Map.of("deleted", id);
	}

	/**
	 * Generate a report (POST — mutating by default, overridden to safe via filter).
	 * @return the report
	 */
	@PostMapping("/reports/generate")
	@Operation(summary = "Generate a report", operationId = "generateReport")
	public Map<String, String> generateReport() {
		return Map.of("report", "data");
	}

	/**
	 * Invalidate the cache (GET — safe by default, overridden to mutating via filter).
	 * @return the result
	 */
	@GetMapping("/cache/invalidate")
	@Operation(summary = "Invalidate the cache", operationId = "invalidateCache")
	public Map<String, String> invalidateCache() {
		return Map.of("invalidated", "true");
	}

}
