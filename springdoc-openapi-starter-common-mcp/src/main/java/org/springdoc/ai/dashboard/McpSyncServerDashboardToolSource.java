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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.ApplicationContext;
import org.springframework.util.ClassUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification;
import io.modelcontextprotocol.spec.McpSchema;
import io.modelcontextprotocol.spec.McpSchema.CallToolRequest;
import io.modelcontextprotocol.spec.McpSchema.CallToolResult;
import io.modelcontextprotocol.spec.McpSchema.TextContent;

/**
 * Dashboard tool source backed by MCP {@link SyncToolSpecification} beans. Discovers
 * tools registered via {@code @McpTool} annotations and other MCP server tool
 * specifications.
 *
 * @author bnasslahsen
 */
public class McpSyncServerDashboardToolSource implements McpDashboardToolSource {

	/**
	 * JSON object mapper.
	 */
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	/**
	 * Type reference for Map deserialization.
	 */
	private static final TypeReference<Map<String, Object>> MAP_TYPE_REF = new TypeReference<>() {
	};

	/**
	 * Map of tool name to sync tool specification.
	 */
	private final Map<String, SyncToolSpecification> specsByName;

	/**
	 * Map of tool name to Java return type, discovered from {@code @McpTool} annotated
	 * methods.
	 */
	private final Map<String, Type> toolReturnTypes;

	/**
	 * Constructs a new McpSyncServerDashboardToolSource.
	 * @param toolSpecifications all sync tool specification lists from the context
	 * @param applicationContext the Spring application context
	 */
	public McpSyncServerDashboardToolSource(List<List<SyncToolSpecification>> toolSpecifications,
			ApplicationContext applicationContext) {
		this.specsByName = new HashMap<>();
		for (List<SyncToolSpecification> specList : toolSpecifications) {
			for (SyncToolSpecification spec : specList) {
				this.specsByName.putIfAbsent(spec.tool().name(), spec);
			}
		}
		this.toolReturnTypes = scanToolReturnTypes(applicationContext);
	}

	@Override
	public List<McpToolInfo> getToolInfos() {
		List<McpToolInfo> tools = new ArrayList<>();
		for (SyncToolSpecification spec : specsByName.values()) {
			McpSchema.Tool tool = spec.tool();
			String inputSchemaJson = convertInputSchema(tool.inputSchema());
			String description = tool.description();
			Type returnType = toolReturnTypes.get(tool.name());
			if (returnType != null && returnType != void.class && returnType != Void.class) {
				String returnDesc = "Returns " + ReturnTypeDescriptionUtils.describeJavaType(returnType);
				description = ReturnTypeDescriptionUtils.appendReturnType(description, returnDesc);
			}
			tools.add(new McpToolInfo(tool.name(), description, inputSchemaJson, null, null, "mcp-tools"));
		}
		return tools;
	}

	@Override
	public McpToolExecutionResponse executeTool(String toolName, String arguments, Map<String, String> extraHeaders) {
		SyncToolSpecification spec = specsByName.get(toolName);
		if (spec == null) {
			return null;
		}
		long start = System.currentTimeMillis();
		try {
			Map<String, Object> argsMap = parseArguments(arguments);
			CallToolResult result;
			if (spec.callHandler() != null) {
				result = spec.callHandler().apply(null, new CallToolRequest(toolName, argsMap));
			}
			else {
				result = spec.call().apply(null, argsMap);
			}
			long duration = System.currentTimeMillis() - start;
			String resultText = extractResultText(result);
			boolean isError = Boolean.TRUE.equals(result.isError());
			return new McpToolExecutionResponse(!isError, resultText, isError ? resultText : null, duration, 0);
		}
		catch (Exception ex) {
			long duration = System.currentTimeMillis() - start;
			return new McpToolExecutionResponse(false, null, ex.getMessage(), duration, 0);
		}
	}

	/**
	 * Converts McpSchema.JsonSchema to a JSON string.
	 * @param inputSchema the input schema
	 * @return JSON string representation
	 */
	private String convertInputSchema(McpSchema.JsonSchema inputSchema) {
		if (inputSchema == null) {
			return "{}";
		}
		try {
			Map<String, Object> schemaMap = new HashMap<>();
			schemaMap.put("type", inputSchema.type());
			if (inputSchema.properties() != null) {
				schemaMap.put("properties", inputSchema.properties());
			}
			if (inputSchema.required() != null) {
				schemaMap.put("required", inputSchema.required());
			}
			if (inputSchema.additionalProperties() != null) {
				schemaMap.put("additionalProperties", inputSchema.additionalProperties());
			}
			return OBJECT_MAPPER.writeValueAsString(schemaMap);
		}
		catch (JsonProcessingException ex) {
			return "{}";
		}
	}

	/**
	 * Parses a JSON arguments string into a Map.
	 * @param arguments the JSON string
	 * @return the parsed map
	 * @throws JsonProcessingException if parsing fails
	 */
	private Map<String, Object> parseArguments(String arguments) throws JsonProcessingException {
		if (arguments == null || arguments.isBlank()) {
			return Map.of();
		}
		return OBJECT_MAPPER.readValue(arguments, MAP_TYPE_REF);
	}

	/**
	 * Extracts text content from a CallToolResult.
	 * @param result the call tool result
	 * @return the extracted text
	 */
	private String extractResultText(CallToolResult result) {
		if (result.content() == null || result.content().isEmpty()) {
			return "";
		}
		return result.content()
			.stream()
			.filter(TextContent.class::isInstance)
			.map(TextContent.class::cast)
			.map(TextContent::text)
			.collect(Collectors.joining("\n"));
	}

	/**
	 * Scans the application context for beans with {@code @McpTool} annotated methods
	 * and builds a map of tool name to return type.
	 * @param applicationContext the Spring application context
	 * @return map of tool name to generic return type
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Type> scanToolReturnTypes(ApplicationContext applicationContext) {
		Map<String, Type> returnTypes = new HashMap<>();
		try {
			Class<? extends Annotation> mcpToolAnnotation = (Class<? extends Annotation>) Class
				.forName("org.springaicommunity.mcp.annotation.McpTool");
			Method nameMethod = mcpToolAnnotation.getMethod("name");
			for (String beanName : applicationContext.getBeanDefinitionNames()) {
				try {
					Class<?> beanType = ClassUtils.getUserClass(applicationContext.getType(beanName));
					for (Method method : beanType.getDeclaredMethods()) {
						Annotation annotation = method.getAnnotation(mcpToolAnnotation);
						if (annotation != null) {
							String toolName = (String) nameMethod.invoke(annotation);
							if (toolName != null && !toolName.isEmpty() && specsByName.containsKey(toolName)) {
								returnTypes.put(toolName, method.getGenericReturnType());
							}
						}
					}
				}
				catch (Exception ex) {
					// Skip beans that cannot be introspected
				}
			}
		}
		catch (ClassNotFoundException ex) {
			// @McpTool annotation not on classpath — no return types available
		}
		catch (Exception ex) {
			// Scanning failed — ignore
		}
		return returnTypes;
	}

}
