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

package org.springdoc.ai.dashboard;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springdoc.ai.mcp.OpenApiToolCallback;

import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;

/**
 * Dashboard tool source backed by Spring AI {@link ToolCallbackProvider} beans. Supports
 * both {@link OpenApiToolCallback} (with HTTP method and path) and generic
 * {@link ToolCallback} instances.
 *
 * @author bnasslahsen
 */
public class ToolCallbackDashboardToolSource implements McpDashboardToolSource {

	/**
	 * The tool callback providers.
	 */
	private final List<ToolCallbackProvider> toolCallbackProviders;

	/**
	 * Constructs a new ToolCallbackDashboardToolSource.
	 * @param toolCallbackProviders the tool callback providers
	 */
	public ToolCallbackDashboardToolSource(List<ToolCallbackProvider> toolCallbackProviders) {
		this.toolCallbackProviders = toolCallbackProviders;
	}

	@Override
	public List<McpToolInfo> getToolInfos() {
		List<McpToolInfo> tools = new ArrayList<>();
		for (ToolCallbackProvider provider : toolCallbackProviders) {
			for (ToolCallback callback : provider.getToolCallbacks()) {
				if (callback instanceof OpenApiToolCallback openApiToolCallback) {
					List<String> tags = openApiToolCallback.getTags();
					String group = (tags != null && !tags.isEmpty()) ? tags.get(0) : null;
					McpToolInfo info = new McpToolInfo(openApiToolCallback.getToolDefinition().name(),
							openApiToolCallback.getToolDefinition().description(),
							openApiToolCallback.getToolDefinition().inputSchema(),
							openApiToolCallback.getMethod().name(), openApiToolCallback.getPath(), group);
					info.setSafe(openApiToolCallback.isSafe());
					info.setRequiresApproval(openApiToolCallback.isRequiresApproval());
					tools.add(info);
				}
				else {
					String description = callback.getToolDefinition().description();
					String returnTypeDescription = buildReturnTypeDescription(callback);
					if (!returnTypeDescription.isEmpty()) {
						description = ReturnTypeDescriptionUtils.appendReturnType(description,
								returnTypeDescription);
					}
					tools.add(new McpToolInfo(callback.getToolDefinition().name(),
							description, callback.getToolDefinition().inputSchema(),
							null, null, "mcp-tools"));
				}
			}
		}
		return tools;
	}

	@Override
	public McpToolExecutionResponse executeTool(String toolName, String arguments, Map<String, String> extraHeaders) {
		return executeTool(toolName, arguments, extraHeaders, false);
	}

	@Override
	public McpToolExecutionResponse executeTool(String toolName, String arguments, Map<String, String> extraHeaders,
			boolean approved) {
		for (ToolCallbackProvider provider : toolCallbackProviders) {
			for (ToolCallback callback : provider.getToolCallbacks()) {
				if (callback.getToolDefinition().name().equals(toolName)) {
					long start = System.currentTimeMillis();
					try {
						if (callback instanceof OpenApiToolCallback openApiToolCallback) {
							HttpResponse<String> httpResponse = approved
									? openApiToolCallback.callWithStatusCodeApproved(arguments, extraHeaders)
									: openApiToolCallback.callWithStatusCode(arguments, extraHeaders);
							long duration = System.currentTimeMillis() - start;
							int statusCode = httpResponse.statusCode();
							boolean success = statusCode >= 200 && statusCode < 300;
							return new McpToolExecutionResponse(success, httpResponse.body(), null, duration,
									statusCode);
						}
						String result = callback.call(arguments);
						long duration = System.currentTimeMillis() - start;
						return new McpToolExecutionResponse(true, result, null, duration, 200);
					}
					catch (OpenApiToolCallback.HumanApprovalRequiredException ex) {
						long duration = System.currentTimeMillis() - start;
						McpToolExecutionResponse response = new McpToolExecutionResponse(false, null, ex.getMessage(),
								duration, 0);
						response.setRequiresApproval(true);
						return response;
					}
					catch (Exception ex) {
						long duration = System.currentTimeMillis() - start;
						return new McpToolExecutionResponse(false, null, ex.getMessage(), duration, 0);
					}
				}
			}
		}
		return null;
	}

	/**
	 * Extracts the return type from a ToolCallback and builds a human-readable
	 * description, similar to
	 * {@link org.springdoc.ai.mcp.OpenApiSchemaConverter#buildResponseDescription}.
	 * @param callback the tool callback
	 * @return the return type description, or empty string if unavailable
	 */
	private static String buildReturnTypeDescription(ToolCallback callback) {
		try {
			Field methodField = callback.getClass().getDeclaredField("toolMethod");
			methodField.setAccessible(true);
			Method method = (Method) methodField.get(callback);
			Type returnType = method.getGenericReturnType();
			if (returnType == void.class || returnType == Void.class) {
				return "";
			}
			return "Returns " + ReturnTypeDescriptionUtils.describeJavaType(returnType);
		}
		catch (Exception ex) {
			return "";
		}
	}

}
