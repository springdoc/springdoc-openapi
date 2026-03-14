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

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import io.swagger.v3.oas.models.PathItem;
import org.junit.jupiter.api.Test;
import org.springdoc.ai.customizers.McpToolCustomizer;
import org.springdoc.ai.mcp.OpenApiMcpToolCallbackProvider;

import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests that {@link McpToolCustomizer} beans can exclude MCP tools by setting
 * {@code context.setExclude(true)} in a WebFlux context.
 *
 * @author bnasslahsen
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestPropertySource(properties = { "springdoc.ai.mcp.enabled=true", "springdoc.ai.mcp.init-timeout-seconds=30",
		"springdoc.pre-loading-enabled=true", "spring.main.lazy-initialization=false" })
class McpEndpointFilterTest {

	/**
	 * The tool callback provider.
	 */
	@Autowired
	private OpenApiMcpToolCallbackProvider toolCallbackProvider;

	/**
	 * The test application with a customizer that excludes DELETE methods and /internal
	 * paths by setting {@code context.setExclude(true)}.
	 */
	@SpringBootApplication
	static class TestApp {

		/**
		 * Registers a customizer that excludes DELETE methods and /internal paths.
		 * @return the MCP tool customizer
		 */
		@Bean
		McpToolCustomizer excludeDeleteAndInternal() {
			return (context, path, method, operation) -> {
				if (method == PathItem.HttpMethod.DELETE || path.startsWith("/internal")) {
					context.setExclude(true);
				}
				return context;
			};
		}

	}

	/**
	 * Verifies that the customizer excludes DELETE and /internal endpoints while keeping
	 * the rest.
	 */
	@Test
	void filtersExcludeDeleteAndInternalEndpoints() {
		ToolCallback[] callbacks = toolCallbackProvider.getToolCallbacks();

		Set<String> toolNames = Arrays.stream(callbacks)
			.map(tc -> tc.getToolDefinition().name())
			.collect(Collectors.toSet());

		assertThat(toolNames).containsExactlyInAnyOrder("list_products", "get_product_by_id", "list_admin_users");
		assertThat(toolNames).doesNotContain("delete_admin_user", "internal_health");
	}

}
