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

import java.util.Arrays;
import java.util.Map;
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
 * Tests that {@link org.springdoc.ai.annotations.McpToolDescription} annotations
 * override tool descriptions and names in a WebFlux context.
 *
 * @author bnasslahsen
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestPropertySource(properties = { "springdoc.ai.mcp.enabled=true", "springdoc.ai.mcp.init-timeout-seconds=30",
		"springdoc.pre-loading-enabled=true", "spring.main.lazy-initialization=false" })
class McpToolDescriptionTest {

	/**
	 * The tool callback provider.
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
	 * Verifies that @McpToolDescription overrides the tool description.
	 */
	@Test
	void annotationOverridesDescription() {
		ToolCallback[] callbacks = toolCallbackProvider.getToolCallbacks();

		Map<String, ToolCallback> toolMap = Arrays.stream(callbacks)
			.collect(Collectors.toMap(tc -> tc.getToolDefinition().name(), tc -> tc));

		assertThat(toolMap.get("list_orders").getToolDefinition().description())
			.isEqualTo("Fetch every order in the system including pending and completed ones");
	}

	/**
	 * Verifies that @McpToolDescription with name attribute overrides both description
	 * and name.
	 */
	@Test
	void annotationOverridesNameAndDescription() {
		ToolCallback[] callbacks = toolCallbackProvider.getToolCallbacks();

		Map<String, ToolCallback> toolMap = Arrays.stream(callbacks)
			.collect(Collectors.toMap(tc -> tc.getToolDefinition().name(), tc -> tc));

		assertThat(toolMap).containsKey("findOrder");
		assertThat(toolMap).doesNotContainKey("get_order_by_id");
		assertThat(toolMap.get("findOrder").getToolDefinition().description())
			.isEqualTo("Look up a single order by its unique identifier");
	}

	/**
	 * Verifies that endpoints without @McpToolDescription keep their default OpenAPI
	 * description.
	 */
	@Test
	void endpointWithoutAnnotationKeepsDefault() {
		ToolCallback[] callbacks = toolCallbackProvider.getToolCallbacks();

		Map<String, ToolCallback> toolMap = Arrays.stream(callbacks)
			.collect(Collectors.toMap(tc -> tc.getToolDefinition().name(), tc -> tc));

		assertThat(toolMap).containsKey("create_order");
		assertThat(toolMap.get("create_order").getToolDefinition().description()).startsWith("Create a new order");
	}

}
