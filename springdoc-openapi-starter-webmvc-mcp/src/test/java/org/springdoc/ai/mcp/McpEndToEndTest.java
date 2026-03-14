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

package org.springdoc.ai.mcp;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;
import org.springdoc.ai.properties.SpringDocAiProperties;
import org.springdoc.core.service.OpenAPIService;

import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.definition.ToolDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * End-to-end test that simulates an AI agent (Claude, GPT, etc.) discovering and invoking
 * API endpoints through the MCP tool callback provider.
 *
 * <p>
 * This test boots a full Spring web context with a {@code @RestController}, enables the
 * MCP integration, and verifies the tool list is correctly served.
 *
 * @author bnasslahsen
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestPropertySource(properties = { "springdoc.ai.mcp.enabled=true", "springdoc.ai.mcp.init-timeout-seconds=30",
		"springdoc.pre-loading-enabled=true", "spring.main.lazy-initialization=false" })
class McpEndToEndTest {

	/**
	 * The object mapper.
	 */
	private final ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * The tool callback provider, auto-wired from Spring context.
	 */
	@Autowired
	private OpenApiMcpToolCallbackProvider toolCallbackProvider;

	/**
	 * The AI properties.
	 */
	@Autowired
	private SpringDocAiProperties aiProperties;

	/**
	 * The OpenAPI service.
	 */
	@Autowired
	private OpenAPIService openAPIService;

	/**
	 * The local server port.
	 */
	@LocalServerPort
	private int port;

	/**
	 * The test application.
	 */
	@SpringBootApplication
	static class TestApp {

	}

	/**
	 * Simulates an AI agent performing tool discovery (the "handshake"). Verifies that
	 * the tool list contains all expected operations from the HelloController.
	 */
	@Test
	void agentDiscoveryReturnsAllTools() {
		ToolCallback[] callbacks = toolCallbackProvider.getToolCallbacks();

		assertThat(callbacks).isNotNull();
		assertThat(callbacks).hasSizeGreaterThanOrEqualTo(3);

		Set<String> toolNames = Arrays.stream(callbacks)
			.map(tc -> tc.getToolDefinition().name())
			.collect(Collectors.toSet());

		assertThat(toolNames).contains("get_user_by_id", "list_users", "create_user");
	}

	/**
	 * Verifies that each tool exposes a proper ToolDefinition with name, description, and
	 * input schema - the metadata an AI agent reads.
	 */
	@Test
	void toolDefinitionsContainMetadata() {
		ToolCallback[] callbacks = toolCallbackProvider.getToolCallbacks();
		Map<String, ToolCallback> toolMap = Arrays.stream(callbacks)
			.collect(Collectors.toMap(tc -> tc.getToolDefinition().name(), tc -> tc));

		// Verify getUserById tool
		ToolCallback getUserTool = toolMap.get("get_user_by_id");
		assertThat(getUserTool).isNotNull();
		ToolDefinition getUserDef = getUserTool.getToolDefinition();
		assertThat(getUserDef.description()).startsWith("Get a user by ID");
		assertThat(getUserDef.inputSchema()).contains("\"id\"");

		// Verify listUsers tool
		ToolCallback listUsersTool = toolMap.get("list_users");
		assertThat(listUsersTool).isNotNull();
		ToolDefinition listUsersDef = listUsersTool.getToolDefinition();
		assertThat(listUsersDef.description()).startsWith("List all users");

		// Verify createUser tool
		ToolCallback createUserTool = toolMap.get("create_user");
		assertThat(createUserTool).isNotNull();
		ToolDefinition createUserDef = createUserTool.getToolDefinition();
		assertThat(createUserDef.description()).startsWith("Create a new user");
		assertThat(createUserDef.inputSchema()).contains("body");
	}

	/**
	 * Verifies that the input schema for a tool with path parameters is well-formed JSON
	 * Schema with correct properties and required fields.
	 * @throws Exception if JSON parsing fails
	 */
	@Test
	void inputSchemaIsValidJsonSchema() throws Exception {
		ToolCallback[] callbacks = toolCallbackProvider.getToolCallbacks();
		ToolCallback getUserTool = Arrays.stream(callbacks)
			.filter(tc -> "get_user_by_id".equals(tc.getToolDefinition().name()))
			.findFirst()
			.orElseThrow();

		String inputSchema = getUserTool.getToolDefinition().inputSchema();
		JsonNode schema = objectMapper.readTree(inputSchema);

		assertThat(schema.get("type").asText()).isEqualTo("object");
		assertThat(schema.has("properties")).isTrue();
		assertThat(schema.get("properties").has("id")).isTrue();

		// id should be required (it's a path variable)
		boolean idRequired = false;
		for (JsonNode req : schema.get("required")) {
			if ("id".equals(req.asText())) {
				idRequired = true;
			}
		}
		assertThat(idRequired).isTrue();
	}

	/**
	 * Simulates an AI agent calling the getUserById tool. Makes a real HTTP request to
	 * the running server and verifies the response.
	 */
	@Test
	void agentCanCallGetUserTool() {
		// Set the base URL to the actual running server and reset the cache
		aiProperties.setBaseUrl("http://localhost:" + port);
		toolCallbackProvider.resetCache();

		ToolCallback[] callbacks = toolCallbackProvider.getToolCallbacks();
		ToolCallback getUserTool = Arrays.stream(callbacks)
			.filter(tc -> "get_user_by_id".equals(tc.getToolDefinition().name()))
			.findFirst()
			.orElseThrow();

		String result = getUserTool.call("{\"id\": \"42\"}");

		assertThat(result).isNotNull();
		assertThat(result).contains("42");
		assertThat(result).contains("John Doe");
	}

	/**
	 * Simulates an AI agent calling the listUsers tool with query parameters.
	 */
	@Test
	void agentCanCallListUsersTool() {
		aiProperties.setBaseUrl("http://localhost:" + port);
		toolCallbackProvider.resetCache();

		ToolCallback[] callbacks = toolCallbackProvider.getToolCallbacks();
		ToolCallback listUsersTool = Arrays.stream(callbacks)
			.filter(tc -> "list_users".equals(tc.getToolDefinition().name()))
			.findFirst()
			.orElseThrow();

		String result = listUsersTool.call("{\"filter\": \"active\"}");

		assertThat(result).isNotNull();
		assertThat(result).contains("John Doe");
	}

	/**
	 * Verifies the OpenAPI spec was pre-loaded (our EnvironmentPostProcessor forces
	 * springdoc.pre-loading-enabled=true when MCP is enabled).
	 */
	@Test
	void openApiIsPreLoaded() {
		OpenAPI cached = openAPIService.getCachedOpenAPI(Locale.getDefault());
		assertThat(cached).isNotNull();
		assertThat(cached.getPaths()).isNotNull();
		assertThat(cached.getPaths()).containsKey("/users/{id}");
	}

	/**
	 * Verifies that operations without operationId are not exposed as tools. All our test
	 * operations have explicit operationIds, so the count should match the number of
	 * annotated endpoints.
	 */
	@Test
	void operationsWithoutIdAreSkipped() {
		OpenAPI cached = openAPIService.getCachedOpenAPI(Locale.getDefault());
		long totalOps = cached.getPaths()
			.values()
			.stream()
			.mapToLong(pathItem -> pathItem.readOperationsMap().size())
			.sum();

		long opsWithId = cached.getPaths()
			.values()
			.stream()
			.flatMap(pathItem -> pathItem.readOperationsMap().values().stream())
			.filter(op -> op.getOperationId() != null)
			.count();

		ToolCallback[] callbacks = toolCallbackProvider.getToolCallbacks();

		// Tools count should equal operations with IDs
		assertThat(callbacks.length).isEqualTo((int) opsWithId);
	}

}
