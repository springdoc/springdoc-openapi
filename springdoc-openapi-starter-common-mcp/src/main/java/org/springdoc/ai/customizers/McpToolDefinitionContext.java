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

package org.springdoc.ai.customizers;

/**
 * Mutable context object that holds the tool definition metadata (name, description,
 * input schema) during the customizer chain. Customizers can modify these values before
 * the final {@link org.springframework.ai.tool.definition.ToolDefinition} is built.
 *
 * <p>
 * In addition to renaming or rewriting tools, a customizer can:
 * <ul>
 * <li>Set {@link #setExclude(boolean) exclude=true} to drop this endpoint from the MCP
 * tool list entirely.</li>
 * <li>Set {@link #setSafeEndpoint(Boolean) safeEndpoint} to override the
 * safe/mutating classification for this specific endpoint, taking precedence over the
 * global {@code springdoc.ai.mcp.guardrails.safe-methods} configuration.</li>
 * </ul>
 *
 * @author bnasslahsen
 */
public class McpToolDefinitionContext {

	/**
	 * The tool name (typically the operationId).
	 */
	private String name;

	/**
	 * The tool description shown to AI agents.
	 */
	private String description;

	/**
	 * The JSON Schema input schema.
	 */
	private String inputSchema;

	/**
	 * When {@code true} this endpoint will be excluded from the MCP tool list.
	 */
	private boolean exclude = false;

	/**
	 * Per-endpoint safe/mutating override. {@code true} = safe, {@code false} = mutating,
	 * {@code null} = use the global {@code springdoc.ai.mcp.guardrails.safe-methods}
	 * configuration.
	 */
	private Boolean safeEndpoint = null;

	/**
	 * Constructs a new McpToolDefinitionContext.
	 * @param name the tool name
	 * @param description the tool description
	 * @param inputSchema the JSON Schema input schema
	 */
	public McpToolDefinitionContext(String name, String description, String inputSchema) {
		this.name = name;
		this.description = description;
		this.inputSchema = inputSchema;
	}

	/**
	 * Gets the tool name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the tool name.
	 * @param name the name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the tool description.
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the tool description.
	 * @param description the description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets the JSON Schema input schema.
	 * @return the input schema
	 */
	public String getInputSchema() {
		return inputSchema;
	}

	/**
	 * Sets the JSON Schema input schema.
	 * @param inputSchema the input schema
	 */
	public void setInputSchema(String inputSchema) {
		this.inputSchema = inputSchema;
	}

	/**
	 * Returns whether this endpoint should be excluded from the MCP tool list.
	 * @return {@code true} if the endpoint should be excluded
	 */
	public boolean isExclude() {
		return exclude;
	}

	/**
	 * Sets whether this endpoint should be excluded from the MCP tool list.
	 * @param exclude {@code true} to exclude the endpoint
	 */
	public void setExclude(boolean exclude) {
		this.exclude = exclude;
	}

	/**
	 * Returns the per-endpoint safe/mutating override, or {@code null} if no override is
	 * set (in which case the global configuration applies).
	 * @return {@code true} = safe, {@code false} = mutating, {@code null} = global default
	 */
	public Boolean getSafeEndpoint() {
		return safeEndpoint;
	}

	/**
	 * Overrides the safe/mutating classification for this specific endpoint.
	 * @param safeEndpoint {@code true} to treat as safe, {@code false} to treat as
	 * mutating, {@code null} to use the global default
	 */
	public void setSafeEndpoint(Boolean safeEndpoint) {
		this.safeEndpoint = safeEndpoint;
	}

}
