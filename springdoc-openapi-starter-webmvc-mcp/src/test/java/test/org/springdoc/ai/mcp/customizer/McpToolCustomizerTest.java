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

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
 * Tests that {@link McpToolCustomizer} beans can rename, rewrite descriptions, and
 * exclude MCP tools.
 *
 * @author bnasslahsen
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestPropertySource(properties = { "springdoc.ai.mcp.enabled=true", "springdoc.ai.mcp.init-timeout-seconds=30",
		"springdoc.pre-loading-enabled=true", "spring.main.lazy-initialization=false" })
class McpToolCustomizerTest {

	/**
	 * The tool callback provider.
	 */
	@Autowired
	private OpenApiMcpToolCallbackProvider toolCallbackProvider;

	/**
	 * The test application with a customizer that renames, rewrites, and excludes tools.
	 */
	@SpringBootApplication
	static class TestApp {

		/**
		 * Registers a customizer that renames listUsers to fetchAllUsers, rewrites
		 * getUserById description, and excludes createUser.
		 * @return the MCP tool customizer
		 */
		@Bean
		McpToolCustomizer testCustomizer() {
			return (context, path, method, operation) -> {
				String operationId = operation.getOperationId();
				if ("listUsers".equals(operationId)) {
					context.setName("fetchAllUsers");
				}
				else if ("getUserById".equals(operationId)) {
					context.setDescription("Retrieve a single user record by their unique identifier");
				}
				else if ("createUser".equals(operationId)) {
					return null;
				}
				return context;
			};
		}

	}

	/**
	 * Verifies that the customizer renames, rewrites, and excludes tools correctly.
	 */
	@Test
	void customizerRenamesRewritesAndExcludesTools() {
		ToolCallback[] callbacks = toolCallbackProvider.getToolCallbacks();

		Set<String> toolNames = Arrays.stream(callbacks)
			.map(tc -> tc.getToolDefinition().name())
			.collect(Collectors.toSet());

		assertThat(toolNames).contains("fetchAllUsers", "get_user_by_id");
		assertThat(toolNames).doesNotContain("list_users", "create_user");

		Map<String, ToolCallback> toolMap = Arrays.stream(callbacks)
			.collect(Collectors.toMap(tc -> tc.getToolDefinition().name(), tc -> tc));

		assertThat(toolMap.get("get_user_by_id").getToolDefinition().description())
			.isEqualTo("Retrieve a single user record by their unique identifier");
	}

}
