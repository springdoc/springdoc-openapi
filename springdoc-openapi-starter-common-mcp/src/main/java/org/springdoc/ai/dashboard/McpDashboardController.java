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

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.swagger.v3.oas.annotations.Operation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for the MCP Developer Dashboard.
 *
 * @author bnasslahsen
 */
@RestController
@RequestMapping("/api/mcp-admin")
public class McpDashboardController {

	/**
	 * The dashboard tool sources.
	 */
	private final List<McpDashboardToolSource> toolSources;

	/**
	 * The in-memory audit event store.
	 */
	private final McpAuditEventStore auditEventStore;

	/**
	 * Constructs a new McpDashboardController.
	 * @param toolSources the dashboard tool sources
	 * @param auditEventStore the in-memory audit event store
	 */
	public McpDashboardController(List<McpDashboardToolSource> toolSources, McpAuditEventStore auditEventStore) {
		this.toolSources = toolSources;
		this.auditEventStore = auditEventStore;
	}

	/**
	 * Lists all available MCP tools from all sources, deduplicated by name.
	 * @return the list of tool info
	 */
	@Operation(hidden = true)
	@GetMapping("/tools")
	public List<McpToolInfo> listTools() {
		Map<String, McpToolInfo> toolsByName = new LinkedHashMap<>();
		for (McpDashboardToolSource source : toolSources) {
			for (McpToolInfo tool : source.getToolInfos()) {
				toolsByName.putIfAbsent(tool.getName(), tool);
			}
		}
		return List.copyOf(toolsByName.values());
	}

	/**
	 * Headers to skip when forwarding to tool execution.
	 */
	private static final Set<String> SKIP_HEADERS = Set.of("content-type", "content-length", "host", "connection",
			"accept", "accept-encoding", "accept-language", "user-agent", "origin", "referer", "cookie",
			"sec-fetch-dest", "sec-fetch-mode", "sec-fetch-site", "sec-ch-ua", "sec-ch-ua-mobile",
			"sec-ch-ua-platform");

	/**
	 * Executes an MCP tool by name, forwarding any authentication headers (Authorization,
	 * API key, etc.) to the underlying API call.
	 * @param request the execution request
	 * @param allHeaders all request headers
	 * @return the execution response
	 */
	@Operation(hidden = true)
	@PostMapping("/tools/execute")
	public ResponseEntity<McpToolExecutionResponse> executeTool(@RequestBody McpToolExecutionRequest request,
			@RequestHeader Map<String, String> allHeaders) {
		Map<String, String> extraHeaders = new HashMap<>();
		for (Map.Entry<String, String> entry : allHeaders.entrySet()) {
			if (!SKIP_HEADERS.contains(entry.getKey().toLowerCase()) && entry.getValue() != null
					&& !entry.getValue().isEmpty()) {
				extraHeaders.put(entry.getKey(), entry.getValue());
			}
		}
		McpToolExecutionResponse firstError = null;
		for (McpDashboardToolSource source : toolSources) {
			McpToolExecutionResponse response = source.executeTool(request.getToolName(), request.getArguments(),
					extraHeaders);
			if (response != null) {
				if (response.isSuccess()) {
					return ResponseEntity.ok(response);
				}
				if (firstError == null) {
					firstError = response;
				}
			}
		}
		if (firstError != null) {
			return ResponseEntity.ok(firstError);
		}
		return ResponseEntity.badRequest()
			.body(new McpToolExecutionResponse(false, null, "Tool not found: " + request.getToolName(), 0, 0));
	}

	/**
	 * Returns the most recent audit events as raw JSON strings, newest first.
	 * @param limit maximum number of events to return (default 200, max 500)
	 * @return list of serialized JSON audit event strings
	 */
	@Operation(hidden = true)
	@GetMapping("/audit")
	public List<String> getAuditEvents(@RequestParam(defaultValue = "200") int limit) {
		return auditEventStore.getRecentEvents(limit);
	}

	/**
	 * Clears all stored audit events from the in-memory store.
	 */
	@Operation(hidden = true)
	@DeleteMapping("/audit")
	public void clearAuditEvents() {
		auditEventStore.clear();
	}

}
