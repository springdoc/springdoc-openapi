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

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

/**
 * Structured JSON audit logger for MCP tool executions.
 *
 * <p>
 * Each tool call produces a single JSON audit event logged at {@code INFO} level to the
 * dedicated logger {@code org.springdoc.ai.mcp.audit}. Configure a separate appender in
 * your logging framework (Logback, Log4j2, …) to route this logger to an audit sink.
 *
 * <p>
 * Identity information is read from the Spring Security context when present (optional
 * dependency). Trace and session IDs are read from SLF4J MDC, where Micrometer Tracing /
 * OpenTelemetry / Brave write them automatically.
 *
 * <p>
 * MDC keys recognised:
 * <ul>
 * <li><b>trace_id</b>: {@code traceId}, {@code X-B3-TraceId}, {@code trace_id}</li>
 * <li><b>session_id</b>: {@code sessionId}, {@code X-Session-ID}, {@code session_id},
 * {@code conversationId}</li>
 * <li><b>client_ip</b>: {@code clientIp}, {@code X-Forwarded-For},
 * {@code remoteAddr}</li>
 * </ul>
 *
 * @author bnasslahsen
 */
public class McpAuditLogger {

	/**
	 * Dedicated logger for audit events — configure a separate appender for this name.
	 */
	private static final Logger AUDIT_LOGGER = LoggerFactory.getLogger("org.springdoc.ai.mcp.audit");

	/**
	 * Shared Jackson mapper for building the JSON audit record.
	 */
	private static final ObjectMapper MAPPER = new ObjectMapper();

	/**
	 * ISO-8601 UTC formatter for the event timestamp.
	 */
	private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter
		.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
		.withZone(ZoneOffset.UTC);

	/**
	 * MDC keys tried in order to resolve the distributed trace ID.
	 */
	private static final String[] TRACE_MDC_KEYS = { "traceId", "X-B3-TraceId", "trace_id" };

	/**
	 * MDC keys tried in order to resolve the session / conversation ID.
	 */
	private static final String[] SESSION_MDC_KEYS = { "sessionId", "X-Session-ID", "session_id", "conversationId" };

	/**
	 * MDC keys tried in order to resolve the caller's client IP address.
	 */
	private static final String[] CLIENT_IP_MDC_KEYS = { "clientIp", "X-Forwarded-For", "remoteAddr" };

	/**
	 * Optional in-memory sink set by {@link org.springdoc.ai.dashboard.McpAuditEventStore}
	 * when the dashboard is active. {@code null} when the dashboard is not in use.
	 */
	private static volatile Consumer<String> eventSink;

	private McpAuditLogger() {
	}

	/**
	 * Registers (or unregisters when {@code null}) a consumer that receives each
	 * serialized JSON audit event immediately after it is written to the SLF4J logger.
	 * Called by {@link org.springdoc.ai.dashboard.McpAuditEventStore}.
	 * @param sink the consumer to register, or {@code null} to unregister
	 */
	public static void setEventSink(Consumer<String> sink) {
		eventSink = sink;
	}

	/**
	 * Writes a structured JSON audit event for the given record.
	 * @param record the audit record describing the tool execution
	 */
	static void log(AuditRecord record) {
		if (!AUDIT_LOGGER.isInfoEnabled()) {
			return;
		}
		try {
			ObjectNode root = MAPPER.createObjectNode();
			root.put("event_type", "MCP_TOOL_EXECUTION");
			root.put("timestamp", ISO_FORMATTER.format(Instant.now()));

			String traceId = resolveFromMdc(TRACE_MDC_KEYS);
			if (traceId != null) {
				root.put("trace_id", traceId);
			}
			String sessionId = resolveFromMdc(SESSION_MDC_KEYS);
			if (sessionId != null) {
				root.put("session_id", sessionId);
			}

			// identity
			ObjectNode identity = root.putObject("identity");
			SecurityInfo sec = extractSecurityInfo();
			identity.put("principal", sec.principal);
			ArrayNode rolesNode = identity.putArray("roles");
			sec.roles.forEach(rolesNode::add);
			String clientIp = resolveFromMdc(CLIENT_IP_MDC_KEYS);
			if (clientIp != null) {
				identity.put("client_ip", clientIp);
			}

			// execution
			ObjectNode exec = root.putObject("execution");
			exec.put("tool_name", record.toolName);
			exec.put("http_method", record.httpMethod);
			exec.put("path_pattern", record.pathPattern);
			if (record.resolvedPath != null) {
				exec.put("resolved_path", record.resolvedPath);
			}
			if (record.hitlStatus != null) {
				exec.put("hitl_status", record.hitlStatus);
			}
			if (record.durationMs >= 0) {
				exec.put("duration_ms", record.durationMs);
			}
			if (record.restDurationMs >= 0) {
				exec.put("rest_duration_ms", record.restDurationMs);
			}

			// outcome
			ObjectNode outcome = root.putObject("outcome");
			outcome.put("status", record.outcomeStatus);
			if (record.httpStatusCode != null) {
				outcome.put("http_status_code", record.httpStatusCode);
			}
			if (record.errorReason != null) {
				outcome.put("error_reason", record.errorReason);
			}

			// translation (LLM → REST mapping)
			if (record.mcpArguments != null || record.requestUrl != null
					|| record.requestBody != null || record.responseBody != null) {
				ObjectNode translation = root.putObject("translation");
				if (record.mcpArgumentsSize > 0) {
					translation.put("mcp_arguments_size", record.mcpArgumentsSize);
				}
				if (record.requestBodySize > 0) {
					translation.put("request_body_size", record.requestBodySize);
				}
				if (record.responseBodySize > 0) {
					translation.put("response_body_size", record.responseBodySize);
				}
				if (record.mcpArguments != null) {
					try {
						translation.set("mcp_arguments", MAPPER.readTree(record.mcpArguments));
					}
					catch (Exception ignored) {
						translation.put("mcp_arguments", record.mcpArguments);
					}
				}
				if (record.requestUrl != null) {
					translation.put("request_url", record.requestUrl);
				}
				if (record.requestBody != null) {
					try {
						translation.set("request_body", MAPPER.readTree(record.requestBody));
					}
					catch (Exception ignored) {
						translation.put("request_body", record.requestBody);
					}
				}
				if (record.responseBody != null) {
					translation.put("response_body", record.responseBody);
				}
			}

			String json = MAPPER.writeValueAsString(root);
			AUDIT_LOGGER.info(json);
			Consumer<String> sink = eventSink;
			if (sink != null) {
				sink.accept(json);
			}
		}
		catch (Exception ex) {
			AUDIT_LOGGER.warn("Failed to serialize MCP audit log entry", ex);
		}
	}

	/**
	 * Returns the first non-blank value found in MDC for the given key candidates, or
	 * {@code null} if none is set.
	 * @param keys MDC keys to probe in order
	 * @return the MDC value or null
	 */
	private static String resolveFromMdc(String[] keys) {
		for (String key : keys) {
			String val = MDC.get(key);
			if (val != null && !val.isBlank()) {
				return val;
			}
		}
		return null;
	}

	/**
	 * Extracts the authenticated principal and roles from the Spring Security context when
	 * Spring Security is present on the classpath. Falls back gracefully to
	 * {@code "anonymous"} with an empty role list.
	 * @return the security info
	 */
	private static SecurityInfo extractSecurityInfo() {
		SecurityInfo info = new SecurityInfo();
		info.principal = "anonymous";
		info.roles = new ArrayList<>();
		try {
			Class<?> holderClass = Class
				.forName("org.springframework.security.core.context.SecurityContextHolder");
			Object context = holderClass.getMethod("getContext").invoke(null);
			Object auth = context.getClass().getMethod("getAuthentication").invoke(context);
			if (auth != null) {
				Object principal = auth.getClass().getMethod("getPrincipal").invoke(auth);
				if (principal != null && !"anonymousUser".equals(principal.toString())) {
					info.principal = auth.getClass().getMethod("getName").invoke(auth).toString();
				}
				Object authorities = auth.getClass().getMethod("getAuthorities").invoke(auth);
				if (authorities instanceof Iterable<?> iterable) {
					for (Object ga : iterable) {
						Object authority = ga.getClass().getMethod("getAuthority").invoke(ga);
						if (authority != null) {
							info.roles.add(authority.toString());
						}
					}
				}
			}
		}
		catch (ClassNotFoundException ignored) {
			// Spring Security not on classpath
		}
		catch (Exception ignored) {
			// No authentication context or reflection failure
		}
		return info;
	}

	/**
	 * Immutable audit record describing a single MCP tool execution.
	 *
	 * @author bnasslahsen
	 */
	static final class AuditRecord {

		/**
		 * The tool name.
		 */
		final String toolName;

		/**
		 * The HTTP method.
		 */
		final String httpMethod;

		/**
		 * The path template (e.g. /books/{id}).
		 */
		final String pathPattern;

		/**
		 * The resolved path after substituting path variables (e.g. /books/42).
		 */
		final String resolvedPath;

		/**
		 * HITL evaluation result: {@code "INTERCEPTED"} when the call was blocked,
		 * {@code "BYPASSED"} when the tool is mutating but HITL is disabled, {@code null}
		 * when HITL does not apply (safe methods).
		 */
		final String hitlStatus;

		/**
		 * Wall-clock execution time in milliseconds, or {@code -1} if not measured.
		 */
		final long durationMs;

		/**
		 * Time spent in the HTTP client send call in milliseconds, or {@code -1} if not measured.
		 */
		final long restDurationMs;

		/**
		 * Byte length of the MCP/JSON-RPC arguments from the LLM.
		 */
		final int mcpArgumentsSize;

		/**
		 * Byte length of the request body sent to the API.
		 */
		final int requestBodySize;

		/**
		 * Byte length of the response body received from the API.
		 */
		final int responseBodySize;

		/**
		 * Outcome status: {@code "SUCCESS"}, {@code "APPROVAL_REQUIRED"}, or
		 * {@code "ERROR"}.
		 */
		final String outcomeStatus;

		/**
		 * HTTP response status code, or {@code null} if no HTTP call was made.
		 */
		final Integer httpStatusCode;

		/**
		 * Error reason string, or {@code null} on success.
		 */
		final String errorReason;

		/**
		 * Raw MCP/JSON-RPC arguments from the LLM (JSON string).
		 */
		final String mcpArguments;

		/**
		 * Full resolved URL sent to the API (baseUrl + path + query).
		 */
		final String requestUrl;

		/**
		 * JSON body sent to the API, or {@code null} for bodyless requests.
		 */
		final String requestBody;

		/**
		 * API response body, truncated to 4 KB.
		 */
		final String responseBody;

		private AuditRecord(Builder b) {
			this.toolName = b.toolName;
			this.httpMethod = b.httpMethod;
			this.pathPattern = b.pathPattern;
			this.resolvedPath = b.resolvedPath;
			this.hitlStatus = b.hitlStatus;
			this.durationMs = b.durationMs;
			this.restDurationMs = b.restDurationMs;
			this.mcpArgumentsSize = b.mcpArgumentsSize;
			this.requestBodySize = b.requestBodySize;
			this.responseBodySize = b.responseBodySize;
			this.outcomeStatus = b.outcomeStatus;
			this.httpStatusCode = b.httpStatusCode;
			this.errorReason = b.errorReason;
			this.mcpArguments = b.mcpArguments;
			this.requestUrl = b.requestUrl;
			this.requestBody = b.requestBody;
			this.responseBody = b.responseBody;
		}

		/**
		 * Creates a new builder.
		 * @return the builder
		 */
		static Builder builder() {
			return new Builder();
		}

		/**
		 * Builder for {@link AuditRecord}.
		 *
		 * @author bnasslahsen
		 */
		static final class Builder {

			private String toolName;

			private String httpMethod;

			private String pathPattern;

			private String resolvedPath;

			private String hitlStatus;

			private long durationMs = -1;

			private long restDurationMs = -1;

			private int mcpArgumentsSize;

			private int requestBodySize;

			private int responseBodySize;

			private String outcomeStatus;

			private Integer httpStatusCode;

			private String errorReason;

			private String mcpArguments;

			private String requestUrl;

			private String requestBody;

			private String responseBody;

			Builder toolName(String v) {
				this.toolName = v;
				return this;
			}

			Builder httpMethod(String v) {
				this.httpMethod = v;
				return this;
			}

			Builder pathPattern(String v) {
				this.pathPattern = v;
				return this;
			}

			Builder resolvedPath(String v) {
				this.resolvedPath = v;
				return this;
			}

			Builder hitlStatus(String v) {
				this.hitlStatus = v;
				return this;
			}

			Builder durationMs(long v) {
				this.durationMs = v;
				return this;
			}

			Builder restDurationMs(long v) {
				this.restDurationMs = v;
				return this;
			}

			Builder mcpArgumentsSize(int v) {
				this.mcpArgumentsSize = v;
				return this;
			}

			Builder requestBodySize(int v) {
				this.requestBodySize = v;
				return this;
			}

			Builder responseBodySize(int v) {
				this.responseBodySize = v;
				return this;
			}

			Builder outcomeStatus(String v) {
				this.outcomeStatus = v;
				return this;
			}

			Builder httpStatusCode(Integer v) {
				this.httpStatusCode = v;
				return this;
			}

			Builder errorReason(String v) {
				this.errorReason = v;
				return this;
			}

			Builder mcpArguments(String v) {
				this.mcpArguments = v;
				return this;
			}

			Builder requestUrl(String v) {
				this.requestUrl = v;
				return this;
			}

			Builder requestBody(String v) {
				this.requestBody = v;
				return this;
			}

			Builder responseBody(String v) {
				this.responseBody = v;
				return this;
			}

			AuditRecord build() {
				return new AuditRecord(this);
			}

		}

	}

	/**
	 * Holder for extracted Spring Security principal and roles.
	 */
	private static class SecurityInfo {

		String principal;

		List<String> roles;

	}

}
