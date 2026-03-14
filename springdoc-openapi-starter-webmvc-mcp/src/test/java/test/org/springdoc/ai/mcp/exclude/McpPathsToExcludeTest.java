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

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springdoc.ai.mcp.OpenApiMcpToolCallbackProvider;

import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the {@code springdoc.ai.mcp.paths-to-exclude} property to verify that excluded
 * paths are not exposed as MCP tools.
 *
 * <p>
 * The {@link ProductController} exposes 5 endpoints across 3 path prefixes:
 * <ul>
 * <li>{@code /products/**} — included (2 endpoints → 2 tools)</li>
 * <li>{@code /admin/**} — excluded (2 endpoints → 0 tools)</li>
 * <li>{@code /internal/**} — excluded (1 endpoint → 0 tools)</li>
 * </ul>
 *
 * @author bnasslahsen
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestPropertySource(properties = { "springdoc.ai.mcp.enabled=true", "springdoc.ai.mcp.init-timeout-seconds=30",
		"springdoc.pre-loading-enabled=true", "spring.main.lazy-initialization=false",
		"springdoc.ai.mcp.paths-to-exclude=/admin,/internal" })
class McpPathsToExcludeTest {

	/**
	 * The tool callback provider, auto-wired from Spring context.
	 */
	@Autowired
	private OpenApiMcpToolCallbackProvider toolCallbackProvider;

	/**
	 * The test application.
	 */
	@SpringBootApplication
	static class TestApp {

	}

	/**
	 * Verifies that only the 2 product endpoints are exposed as tools and the 3 excluded
	 * endpoints (admin, internal) are not.
	 */
	@Test
	void onlyNonExcludedPathsAreExposedAsTools() {
		ToolCallback[] callbacks = toolCallbackProvider.getToolCallbacks();

		Set<String> toolNames = Arrays.stream(callbacks)
			.map(tc -> tc.getToolDefinition().name())
			.collect(Collectors.toSet());

		// Product endpoints should be included
		assertThat(toolNames).contains("list_products", "get_product_by_id");

		// Admin and internal endpoints should be excluded
		assertThat(toolNames).doesNotContain("list_admin_users", "delete_admin_user", "get_health_status");
	}

	/**
	 * Verifies that exactly 2 tools are registered (matching the 2 non-excluded
	 * endpoints).
	 */
	@Test
	void exactToolCountMatchesNonExcludedEndpoints() {
		ToolCallback[] callbacks = toolCallbackProvider.getToolCallbacks();
		assertThat(callbacks).hasSize(2);
	}

	/**
	 * Verifies that included tools have proper metadata (description and input schema).
	 */
	@Test
	void includedToolsHaveProperMetadata() {
		ToolCallback[] callbacks = toolCallbackProvider.getToolCallbacks();
		Map<String, ToolCallback> toolMap = Arrays.stream(callbacks)
			.collect(Collectors.toMap(tc -> tc.getToolDefinition().name(), tc -> tc));

		ToolCallback listProducts = toolMap.get("list_products");
		assertThat(listProducts.getToolDefinition().description()).startsWith("List all products");

		ToolCallback getProduct = toolMap.get("get_product_by_id");
		assertThat(getProduct.getToolDefinition().description()).startsWith("Get a product by ID");
		assertThat(getProduct.getToolDefinition().inputSchema()).contains("\"id\"");
	}

}
