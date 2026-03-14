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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.ai.customizers.McpToolCustomizer;
import org.springdoc.ai.customizers.McpToolDefinitionContext;
import org.springdoc.ai.properties.SpringDocAiProperties;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.service.OpenAPIService;

import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;

/**
 * A {@link ToolCallbackProvider} that bridges springdoc-openapi with Spring AI. It
 * converts OpenAPI operations into AI tool callbacks that can be used by AI agents for
 * API discovery and invocation.
 *
 * <p>
 * This provider is non-blocking: {@link #getToolCallbacks()} returns immediately, either
 * from cache or with an empty array if the OpenAPI specification is not yet available.
 * When used with an MCP server, tools are registered dynamically after all singleton
 * beans are instantiated via
 * {@link org.springdoc.ai.configuration.SpringDocAiAutoConfiguration}.
 *
 * @author bnasslahsen
 */
public class OpenApiMcpToolCallbackProvider implements ToolCallbackProvider {

	/**
	 * The logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(OpenApiMcpToolCallbackProvider.class);

	/**
	 * The OpenAPI service.
	 */
	private final OpenAPIService openAPIService;

	/**
	 * The springdoc configuration properties.
	 */
	private final SpringDocConfigProperties springDocConfigProperties;

	/**
	 * The AI properties.
	 */
	private final SpringDocAiProperties aiProperties;

	/**
	 * The MCP tool customizers.
	 */
	private final Optional<List<McpToolCustomizer>> mcpToolCustomizers;

	/**
	 * Cached tool callbacks, built lazily from the OpenAPI specification.
	 */
	private volatile ToolCallback[] cachedToolCallbacks;

	/**
	 * Constructs a new OpenApiMcpToolCallbackProvider.
	 * @param openAPIService the OpenAPI service
	 * @param springDocConfigProperties the springdoc config properties
	 * @param aiProperties the AI properties
	 * @param mcpToolCustomizers the MCP tool customizers
	 */
	public OpenApiMcpToolCallbackProvider(OpenAPIService openAPIService,
			SpringDocConfigProperties springDocConfigProperties, SpringDocAiProperties aiProperties,
			Optional<List<McpToolCustomizer>> mcpToolCustomizers) {
		this.openAPIService = openAPIService;
		this.springDocConfigProperties = springDocConfigProperties;
		this.aiProperties = aiProperties;
		this.mcpToolCustomizers = mcpToolCustomizers;
	}

	@Override
	public ToolCallback[] getToolCallbacks() {
		ToolCallback[] cached = this.cachedToolCallbacks;
		if (cached != null) {
			return cached;
		}
		OpenAPI openAPI = openAPIService.getCachedOpenAPI(Locale.getDefault());
		if (openAPI != null) {
			cached = buildToolCallbacks(openAPI);
			this.cachedToolCallbacks = cached;
			return cached;
		}
		return new ToolCallback[0];
	}

	/**
	 * Iterates OpenAPI paths and creates one {@link OpenApiToolCallback} per operation.
	 * @param openAPI the OpenAPI spec
	 * @return the array of tool callbacks
	 */
	ToolCallback[] buildToolCallbacks(OpenAPI openAPI) {
		List<ToolCallback> tools = new ArrayList<>();
		String baseUrl = resolveBaseUrl();

		if (openAPI.getPaths() == null) {
			return new ToolCallback[0];
		}

		List<String> pathsToExclude = aiProperties.getPathsToExclude();

		for (Map.Entry<String, PathItem> pathEntry : openAPI.getPaths().entrySet()) {
			String path = pathEntry.getKey();

			if (pathsToExclude != null && pathsToExclude.stream().anyMatch(path::startsWith)) {
				continue;
			}

			PathItem pathItem = pathEntry.getValue();
			pathItem.readOperationsMap().forEach((httpMethod, operation) -> {
				if (operation.getOperationId() != null) {
					McpToolDefinitionContext context = OpenApiToolCallback.buildDefaultContext(path, httpMethod,
							operation, openAPI.getComponents());
					if (mcpToolCustomizers.isPresent()) {
						for (McpToolCustomizer customizer : mcpToolCustomizers.get()) {
							context = customizer.customize(context, path, httpMethod, operation);
							if (context == null) {
								return;
							}
						}
					}
					if (context.isExclude()) {
						return;
					}
					Optional<Boolean> safeOverride = Optional.ofNullable(context.getSafeEndpoint());
					tools.add(new OpenApiToolCallback(context, path, httpMethod, operation, openAPI.getComponents(),
							baseUrl, aiProperties, safeOverride));
				}
			});
		}

		LOGGER.info("Registered {} MCP tool callbacks from OpenAPI specification", tools.size());
		return tools.toArray(new ToolCallback[0]);
	}

	/**
	 * Clears the cached tool callbacks so that the next call to
	 * {@link #getToolCallbacks()} rebuilds them.
	 */
	void resetCache() {
		this.cachedToolCallbacks = null;
	}

	/**
	 * Resolves the base URL for HTTP calls.
	 * @return the base URL
	 */
	private String resolveBaseUrl() {
		if (aiProperties.getBaseUrl() != null && !aiProperties.getBaseUrl().isEmpty()) {
			return aiProperties.getBaseUrl();
		}
		return "http://localhost:8080";
	}

}
