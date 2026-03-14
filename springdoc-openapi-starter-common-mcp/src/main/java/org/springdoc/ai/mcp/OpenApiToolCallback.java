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

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem.HttpMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.ai.customizers.McpToolDefinitionContext;
import org.springdoc.ai.properties.SpringDocAiProperties;

import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.definition.DefaultToolDefinition;
import org.springframework.ai.tool.definition.ToolDefinition;

/**
 * A {@link ToolCallback} implementation that represents a single OpenAPI operation as an
 * AI tool. When called, it constructs and executes an HTTP request to the corresponding
 * API endpoint.
 *
 * @author bnasslahsen
 */
public class OpenApiToolCallback implements ToolCallback {

	/**
	 * The logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(OpenApiToolCallback.class);

	/**
	 * The object mapper for JSON parsing.
	 */
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	/**
	 * The HTTP client for making requests.
	 */
	private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
		.connectTimeout(Duration.ofSeconds(30))
		.build();

	/**
	 * Pending approvals store: keys are "toolName:toolInput" awaiting human approval.
	 */
	private static final Set<String> PENDING_APPROVALS = ConcurrentHashMap.newKeySet();

	/**
	 * The path template (e.g. /users/{id}).
	 */
	private final String path;

	/**
	 * The HTTP method.
	 */
	private final HttpMethod method;

	/**
	 * The OpenAPI operation.
	 */
	private final Operation operation;

	/**
	 * The OpenAPI components for schema resolution.
	 */
	private final Components components;

	/**
	 * The base URL for HTTP requests.
	 */
	private final String baseUrl;

	/**
	 * The tool definition.
	 */
	private final ToolDefinition toolDefinition;

	/**
	 * The AI properties for guardrails configuration.
	 */
	private final SpringDocAiProperties aiProperties;

	/**
	 * Whether this tool uses a safe (read-only) HTTP method.
	 */
	private final boolean safe;

	/**
	 * Constructs a new OpenApiToolCallback.
	 * @param path the path template
	 * @param method the HTTP method
	 * @param operation the OpenAPI operation
	 * @param components the OpenAPI components
	 * @param baseUrl the base URL
	 */
	public OpenApiToolCallback(String path, HttpMethod method, Operation operation, Components components,
			String baseUrl) {
		this(buildDefaultContext(path, method, operation, components), path, method, operation, components, baseUrl,
				new SpringDocAiProperties());
	}

	/**
	 * Constructs a new OpenApiToolCallback from a customized context.
	 * @param context the tool definition context (name, description, inputSchema)
	 * @param path the path template
	 * @param method the HTTP method
	 * @param operation the OpenAPI operation
	 * @param components the OpenAPI components
	 * @param baseUrl the base URL
	 */
	public OpenApiToolCallback(McpToolDefinitionContext context, String path, HttpMethod method, Operation operation,
			Components components, String baseUrl) {
		this(context, path, method, operation, components, baseUrl, new SpringDocAiProperties());
	}

	/**
	 * Constructs a new OpenApiToolCallback from a customized context with guardrails.
	 * @param context the tool definition context (name, description, inputSchema)
	 * @param path the path template
	 * @param method the HTTP method
	 * @param operation the OpenAPI operation
	 * @param components the OpenAPI components
	 * @param baseUrl the base URL
	 * @param aiProperties the AI properties for guardrails configuration
	 */
	public OpenApiToolCallback(McpToolDefinitionContext context, String path, HttpMethod method, Operation operation,
			Components components, String baseUrl, SpringDocAiProperties aiProperties) {
		this(context, path, method, operation, components, baseUrl, aiProperties, Optional.empty());
	}

	/**
	 * Constructs a new OpenApiToolCallback from a customized context with guardrails and
	 * a per-endpoint safety override.
	 * @param context the tool definition context (name, description, inputSchema)
	 * @param path the path template
	 * @param method the HTTP method
	 * @param operation the OpenAPI operation
	 * @param components the OpenAPI components
	 * @param baseUrl the base URL
	 * @param aiProperties the AI properties for guardrails configuration
	 * @param safeOverride per-endpoint safety override set via
	 * {@link org.springdoc.ai.customizers.McpToolDefinitionContext#setSafeEndpoint}; when
	 * non-empty it takes precedence over the global {@code safe-methods} configuration
	 */
	public OpenApiToolCallback(McpToolDefinitionContext context, String path, HttpMethod method, Operation operation,
			Components components, String baseUrl, SpringDocAiProperties aiProperties,
			Optional<Boolean> safeOverride) {
		this.path = path;
		this.method = method;
		this.operation = operation;
		this.components = components;
		this.baseUrl = baseUrl;
		this.aiProperties = aiProperties;
		this.safe = safeOverride.orElseGet(
				() -> aiProperties.getGuardrails().getSafeMethods().contains(method.name().toUpperCase()));
		this.toolDefinition = DefaultToolDefinition.builder()
			.name(context.getName())
			.description(context.getDescription())
			.inputSchema(context.getInputSchema())
			.build();
	}

	/**
	 * Builds the default tool definition context from an OpenAPI operation. Computes the
	 * tool name, description, and input schema from the operation metadata.
	 * @param path the path template
	 * @param method the HTTP method
	 * @param operation the OpenAPI operation
	 * @param components the OpenAPI components
	 * @return the default tool definition context
	 */
	public static McpToolDefinitionContext buildDefaultContext(String path, HttpMethod method, Operation operation,
			Components components) {
		String summary = operation.getSummary();
		String opDescription = operation.getDescription();
		String description;
		if (summary != null && !summary.isEmpty() && opDescription != null && !opDescription.isEmpty()) {
			description = summary + " - " + opDescription;
		}
		else if (summary != null && !summary.isEmpty()) {
			description = summary;
		}
		else if (opDescription != null && !opDescription.isEmpty()) {
			description = opDescription;
		}
		else {
			description = method.name() + " " + path;
		}

		String responseDescription = OpenApiSchemaConverter.buildResponseDescription(operation, components);
		if (!responseDescription.isEmpty()) {
			description = description + ". " + responseDescription;
		}

		String inputSchema = OpenApiSchemaConverter.buildInputSchema(path, operation, components);

		return new McpToolDefinitionContext(toSnakeCase(operation.getOperationId()), description, inputSchema);
	}

	/**
	 * Converts a camelCase or PascalCase string to snake_case for better LLM
	 * tokenization.
	 * @param name the input string
	 * @return the snake_case string
	 */
	static String toSnakeCase(String name) {
		if (name == null || name.isEmpty()) {
			return name;
		}
		return name.replaceAll("([a-z\\d])([A-Z])", "$1_$2")
			.replaceAll("([A-Z]+)([A-Z][a-z])", "$1_$2")
			.toLowerCase();
	}

	@Override
	public ToolDefinition getToolDefinition() {
		return toolDefinition;
	}

	/**
	 * Gets path.
	 * @return the path template
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Gets method.
	 * @return the HTTP method
	 */
	public HttpMethod getMethod() {
		return method;
	}

	/**
	 * Gets base url.
	 * @return the base URL
	 */
	public String getBaseUrl() {
		return baseUrl;
	}

	/**
	 * Gets the tags from the OpenAPI operation.
	 * @return the list of tags, or null if none
	 */
	public List<String> getTags() {
		return operation.getTags();
	}

	/**
	 * Returns whether this tool uses a safe (read-only) HTTP method.
	 * @return true if the HTTP method is safe (GET, HEAD, OPTIONS by default)
	 */
	public boolean isSafe() {
		return safe;
	}

	/**
	 * Returns whether this tool requires human approval before execution.
	 * @return true if the tool is mutating and guardrails require approval
	 */
	public boolean isRequiresApproval() {
		return !safe && aiProperties.getGuardrails().isRequireApprovalForMutatingTools();
	}

	@Override
	public String call(String toolInput) {
		if (!safe && aiProperties.getGuardrails().isRequireApprovalForMutatingTools()) {
			String approvalKey = getToolDefinition().name() + ":" + (toolInput != null ? toolInput : "{}");
			if (PENDING_APPROVALS.remove(approvalKey)) {
				return executeHttp(toolInput, null, "APPROVED").body();
			}
			PENDING_APPROVALS.add(approvalKey);
			McpAuditLogger.log(McpAuditLogger.AuditRecord.builder()
				.toolName(getToolDefinition().name())
				.httpMethod(method.name())
				.pathPattern(path)
				.hitlStatus("INTERCEPTED")
				.outcomeStatus("APPROVAL_REQUIRED")
				.build());
			return buildApprovalRequiredJson(toolInput);
		}
		return executeHttp(toolInput, null, !safe ? "BYPASSED" : null).body();
	}

	/**
	 * Builds a structured JSON response indicating that human approval is required.
	 * @param toolInput the original tool input JSON
	 * @return JSON string with requires_human_approval flag
	 */
	private String buildApprovalRequiredJson(String toolInput) {
		return String.format(
				"{\"requires_human_approval\":true,\"tool_name\":\"%s\",\"http_method\":\"%s\","
						+ "\"path\":\"%s\",\"arguments\":%s,"
						+ "\"message\":\"This mutating operation (%s %s) requires human approval before execution.\"}",
				getToolDefinition().name(), method.name(), path, (toolInput != null ? toolInput : "{}"),
				method.name(), path);
	}

	/**
	 * Executes the tool and returns the full HTTP response including status code.
	 * @param toolInput the tool input JSON string
	 * @return the HTTP response with body and status code
	 */
	public HttpResponse<String> callWithStatusCode(String toolInput) {
		return callWithStatusCode(toolInput, null);
	}

	/**
	 * Executes the tool and returns the full HTTP response including status code, with
	 * additional headers applied to the request.
	 * @param toolInput the tool input JSON string
	 * @param extraHeaders additional headers to include (e.g. Authorization)
	 * @return the HTTP response with body and status code
	 */
	public HttpResponse<String> callWithStatusCode(String toolInput, Map<String, String> extraHeaders) {
		if (!safe && aiProperties.getGuardrails().isRequireApprovalForMutatingTools()) {
			McpAuditLogger.log(McpAuditLogger.AuditRecord.builder()
				.toolName(getToolDefinition().name())
				.httpMethod(method.name())
				.pathPattern(path)
				.hitlStatus("INTERCEPTED")
				.outcomeStatus("APPROVAL_REQUIRED")
				.build());
			throw new HumanApprovalRequiredException("Human approval required for " + method.name() + " " + path);
		}
		return executeHttp(toolInput, extraHeaders, !safe ? "BYPASSED" : null);
	}

	/**
	 * Performs the actual HTTP call without any approval guard. Used internally by
	 * {@link #call} (after approval) and {@link #callWithStatusCode} (when guardrails are
	 * disabled).
	 * @param toolInput the tool input JSON string
	 * @param extraHeaders additional headers to include (e.g. Authorization)
	 * @param hitlStatus the HITL status to record in the audit log (e.g. {@code "APPROVED"},
	 * {@code "BYPASSED"}, or {@code null} for safe methods)
	 * @return the HTTP response with body and status code
	 */
	private HttpResponse<String> executeHttp(String toolInput, Map<String, String> extraHeaders, String hitlStatus) {
		long start = System.currentTimeMillis();
		try {
			JsonNode input = OBJECT_MAPPER.readTree(toolInput != null ? toolInput : "{}");
			String resolvedPath = resolvePath(input);
			String queryString = buildQueryString(input);
			String url = baseUrl + resolvedPath;
			if (!queryString.isEmpty()) {
				url = url + "?" + queryString;
			}

			HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
				.uri(URI.create(url))
				.timeout(Duration.ofSeconds(30));

			addHeaders(requestBuilder, input);

			if (extraHeaders != null) {
				extraHeaders.forEach(requestBuilder::header);
			}

			String bodyString = buildBodyString(input);
			HttpRequest.BodyPublisher bodyPublisher = bodyString != null
					? HttpRequest.BodyPublishers.ofString(bodyString)
					: HttpRequest.BodyPublishers.noBody();
			requestBuilder.method(method.name(), bodyPublisher);
			if (bodyString != null) {
				requestBuilder.header("Content-Type", "application/json");
			}

			long restStart = System.currentTimeMillis();
			HttpResponse<String> response = HTTP_CLIENT.send(requestBuilder.build(),
					HttpResponse.BodyHandlers.ofString());
			long restDurationMs = System.currentTimeMillis() - restStart;
			McpAuditLogger.log(McpAuditLogger.AuditRecord.builder()
				.toolName(getToolDefinition().name())
				.httpMethod(method.name())
				.pathPattern(path)
				.resolvedPath(resolvedPath)
				.hitlStatus(hitlStatus)
				.durationMs(System.currentTimeMillis() - start)
				.restDurationMs(restDurationMs)
				.outcomeStatus("SUCCESS")
				.httpStatusCode(response.statusCode())
				.mcpArguments(toolInput)
				.mcpArgumentsSize(byteLength(toolInput))
				.requestUrl(url)
				.requestBody(bodyString)
				.requestBodySize(byteLength(bodyString))
				.responseBody(truncate(response.body(), 4096))
				.responseBodySize(byteLength(response.body()))
				.build());
			return response;
		}
		catch (IOException | InterruptedException ex) {
			McpAuditLogger.log(McpAuditLogger.AuditRecord.builder()
				.toolName(getToolDefinition().name())
				.httpMethod(method.name())
				.pathPattern(path)
				.hitlStatus(hitlStatus)
				.durationMs(System.currentTimeMillis() - start)
				.outcomeStatus("ERROR")
				.errorReason(ex.getClass().getSimpleName() + ": " + ex.getMessage())
				.mcpArguments(toolInput)
				.build());
			if (ex instanceof InterruptedException) {
				Thread.currentThread().interrupt();
			}
			throw new ToolExecutionException(ex.getMessage(), ex);
		}
	}

	/**
	 * Exception thrown when a tool execution fails.
	 *
	 * @author bnasslahsen
	 */
	public static class ToolExecutionException extends RuntimeException {

		/**
		 * Constructs a new ToolExecutionException.
		 * @param message the error message
		 * @param cause the cause
		 */
		public ToolExecutionException(String message, Throwable cause) {
			super(message, cause);
		}

	}

	/**
	 * Exception thrown when a mutating tool requires human approval before execution.
	 *
	 * @author bnasslahsen
	 */
	public static class HumanApprovalRequiredException extends ToolExecutionException {

		/**
		 * Constructs a new HumanApprovalRequiredException.
		 * @param message the error message
		 */
		public HumanApprovalRequiredException(String message) {
			super(message, null);
		}

	}

	/**
	 * Pattern to match path template variables like {id} or {version}.
	 */
	private static final Pattern PATH_VARIABLE_PATTERN = Pattern.compile("\\{([^}]+)}");

	/**
	 * Resolves path parameters in the URL template. Handles both declared parameters and
	 * undeclared path template variables from the input.
	 * @param input the tool input
	 * @return the resolved path
	 */
	private String resolvePath(JsonNode input) {
		String resolved = path;
		if (operation.getParameters() != null) {
			for (io.swagger.v3.oas.models.parameters.Parameter param : operation.getParameters()) {
				if ("path".equals(param.getIn()) && input.has(param.getName())) {
					resolved = resolved.replace("{" + param.getName() + "}", input.get(param.getName()).asText());
				}
			}
		}
		Matcher matcher = PATH_VARIABLE_PATTERN.matcher(resolved);
		StringBuilder sb = new StringBuilder();
		while (matcher.find()) {
			String varName = matcher.group(1);
			String replacement = input.has(varName) ? input.get(varName).asText() : "";
			matcher.appendReplacement(sb, Matcher.quoteReplacement(replacement));
		}
		matcher.appendTail(sb);
		return sb.toString();
	}

	/**
	 * Builds query string from query parameters.
	 * @param input the tool input
	 * @return the query string
	 */
	private String buildQueryString(JsonNode input) {
		StringBuilder sb = new StringBuilder();
		if (operation.getParameters() != null) {
			for (io.swagger.v3.oas.models.parameters.Parameter param : operation.getParameters()) {
				if ("query".equals(param.getIn()) && input.has(param.getName())) {
					if (sb.length() > 0) {
						sb.append('&');
					}
					sb.append(URLEncoder.encode(param.getName(), StandardCharsets.UTF_8))
						.append('=')
						.append(URLEncoder.encode(input.get(param.getName()).asText(), StandardCharsets.UTF_8));
				}
			}
		}
		return sb.toString();
	}

	/**
	 * Adds header parameters to the request.
	 * @param requestBuilder the HTTP request builder
	 * @param input the tool input
	 */
	private void addHeaders(HttpRequest.Builder requestBuilder, JsonNode input) {
		if (operation.getParameters() != null) {
			for (io.swagger.v3.oas.models.parameters.Parameter param : operation.getParameters()) {
				if ("header".equals(param.getIn()) && input.has(param.getName())) {
					requestBuilder.header(param.getName(), input.get(param.getName()).asText());
				}
			}
		}
	}

	/**
	 * Builds the request body JSON string from the input.
	 * @param input the tool input
	 * @return the body JSON string, or {@code null} when there is no body
	 */
	private String buildBodyString(JsonNode input) {
		if (operation.getRequestBody() != null && input.has("body")) {
			return input.get("body").toString();
		}
		// If there's a request body expected but no "body" key, try using all
		// non-parameter fields
		if (operation.getRequestBody() != null) {
			com.fasterxml.jackson.databind.node.ObjectNode bodyNode = OBJECT_MAPPER.createObjectNode();
			Iterator<Map.Entry<String, JsonNode>> fields = input.fields();
			while (fields.hasNext()) {
				Map.Entry<String, JsonNode> field = fields.next();
				if (!isParameterName(field.getKey())) {
					bodyNode.set(field.getKey(), field.getValue());
				}
			}
			if (!bodyNode.isEmpty()) {
				return bodyNode.toString();
			}
		}
		return null;
	}

	/**
	 * Returns the UTF-8 byte length of a string, or {@code 0} if the string is {@code null}.
	 * @param s the string (may be {@code null})
	 * @return the byte length
	 */
	private static int byteLength(String s) {
		return s == null ? 0 : s.getBytes(StandardCharsets.UTF_8).length;
	}

	/**
	 * Truncates a string to the given maximum length.
	 * @param value the string to truncate (may be {@code null})
	 * @param maxLen the maximum number of characters
	 * @return the truncated string, or {@code null} if the input is {@code null}
	 */
	private static String truncate(String value, int maxLen) {
		if (value == null || value.length() <= maxLen) {
			return value;
		}
		return value.substring(0, maxLen);
	}

	/**
	 * Checks if the given name is a declared parameter name.
	 * @param name the field name
	 * @return true if it's a parameter name
	 */
	private boolean isParameterName(String name) {
		if (operation.getParameters() == null) {
			return false;
		}
		return operation.getParameters().stream().anyMatch(p -> name.equals(p.getName()));
	}

}
