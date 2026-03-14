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

package org.springdoc.ai.properties;

import java.util.List;

import org.springdoc.core.configuration.SpringDocConfiguration;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Lazy;

/**
 * The type Spring doc ai properties.
 *
 * @author bnasslahsen
 */
@Lazy(false)
@AutoConfiguration(after = SpringDocConfiguration.class)
@ConfigurationProperties(prefix = "springdoc.ai.mcp")
@ConditionalOnProperty(name = "springdoc.ai.mcp.enabled", matchIfMissing = true)
@ConditionalOnBean(SpringDocConfiguration.class)
public class SpringDocAiProperties {

	/**
	 * The property name for enabling the MCP AI integration.
	 */
	public static final String SPRINGDOC_MCP_ENABLED = "springdoc.ai.mcp.enabled";

	/**
	 * Enable the MCP Tool Server.
	 */
	private boolean enabled;

	/**
	 * Base URL for tool execution HTTP calls. Default: auto-detected from server.port.
	 */
	private String baseUrl;

	/**
	 * Timeout in seconds waiting for OpenAPI to be available. Default: 30.
	 */
	private int initTimeoutSeconds = 30;

	/**
	 * Paths to exclude from MCP tool generation (ant patterns).
	 */
	private List<String> pathsToExclude;

	/**
	 * The MCP endpoint path. Default: /mcp.
	 */
	private String mcpEndpoint = "/mcp";

	/**
	 * The property name for enabling the MCP Dashboard UI.
	 */
	public static final String SPRINGDOC_MCP_UI_ENABLED = "springdoc.ai.mcp.dashboard-enabled";

	/**
	 * Enable the MCP Developer Dashboard.
	 */
	private boolean dashboardEnabled;

	/**
	 * The dashboard UI mount path. Default: /mcp-ui.
	 */
	private String dashboardPath = "/mcp-ui";

	/**
	 * Gets enabled.
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Sets enabled.
	 * @param enabled the enabled
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * Gets base url.
	 * @return the base url
	 */
	public String getBaseUrl() {
		return baseUrl;
	}

	/**
	 * Sets base url.
	 * @param baseUrl the base url
	 */
	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	/**
	 * Gets init timeout seconds.
	 * @return the init timeout seconds
	 */
	public int getInitTimeoutSeconds() {
		return initTimeoutSeconds;
	}

	/**
	 * Sets init timeout seconds.
	 * @param initTimeoutSeconds the init timeout seconds
	 */
	public void setInitTimeoutSeconds(int initTimeoutSeconds) {
		this.initTimeoutSeconds = initTimeoutSeconds;
	}

	/**
	 * Gets paths to exclude.
	 * @return the paths to exclude
	 */
	public List<String> getPathsToExclude() {
		return pathsToExclude;
	}

	/**
	 * Sets paths to exclude.
	 * @param pathsToExclude the paths to exclude
	 */
	public void setPathsToExclude(List<String> pathsToExclude) {
		this.pathsToExclude = pathsToExclude;
	}

	/**
	 * Gets mcp endpoint.
	 * @return the mcp endpoint
	 */
	public String getMcpEndpoint() {
		return mcpEndpoint;
	}

	/**
	 * Sets mcp endpoint.
	 * @param mcpEndpoint the mcp endpoint
	 */
	public void setMcpEndpoint(String mcpEndpoint) {
		this.mcpEndpoint = mcpEndpoint;
	}

	/**
	 * Gets dashboard enabled.
	 * @return the dashboard enabled
	 */
	public boolean isDashboardEnabled() {
		return dashboardEnabled;
	}

	/**
	 * Sets dashboard enabled.
	 * @param dashboardEnabled the dashboard enabled
	 */
	public void setDashboardEnabled(boolean dashboardEnabled) {
		this.dashboardEnabled = dashboardEnabled;
	}

	/**
	 * Gets dashboard path.
	 * @return the dashboard path
	 */
	public String getDashboardPath() {
		return dashboardPath;
	}

	/**
	 * Sets dashboard path.
	 * @param dashboardPath the dashboard path
	 */
	public void setDashboardPath(String dashboardPath) {
		this.dashboardPath = dashboardPath;
	}

	/**
	 * Guardrails configuration for MCP tool safety.
	 */
	private Guardrails guardrails = new Guardrails();

	/**
	 * Gets guardrails.
	 * @return the guardrails
	 */
	public Guardrails getGuardrails() {
		return guardrails;
	}

	/**
	 * Sets guardrails.
	 * @param guardrails the guardrails
	 */
	public void setGuardrails(Guardrails guardrails) {
		this.guardrails = guardrails;
	}

	/**
	 * Guardrails configuration class for MCP tool safety classification and
	 * human-in-the-loop (HITL) support.
	 *
	 * @author bnasslahsen
	 */
	public static class Guardrails {

		/**
		 * When true, mutating tools (non-safe HTTP methods) return a "requires approval"
		 * response instead of executing the HTTP call.
		 */
		private boolean requireApprovalForMutatingTools = true;

		/**
		 * HTTP methods considered safe (read-only). Anything not in this list is
		 * mutating. Default: GET, HEAD, OPTIONS.
		 */
		private List<String> safeMethods = List.of("GET", "HEAD", "OPTIONS");

		/**
		 * Gets require approval for mutating tools.
		 * @return the require approval for mutating tools
		 */
		public boolean isRequireApprovalForMutatingTools() {
			return requireApprovalForMutatingTools;
		}

		/**
		 * Sets require approval for mutating tools.
		 * @param requireApprovalForMutatingTools the require approval for mutating tools
		 */
		public void setRequireApprovalForMutatingTools(boolean requireApprovalForMutatingTools) {
			this.requireApprovalForMutatingTools = requireApprovalForMutatingTools;
		}

		/**
		 * Gets safe methods.
		 * @return the safe methods
		 */
		public List<String> getSafeMethods() {
			return safeMethods;
		}

		/**
		 * Sets safe methods.
		 * @param safeMethods the safe methods
		 */
		public void setSafeMethods(List<String> safeMethods) {
			this.safeMethods = safeMethods;
		}

	}

}
