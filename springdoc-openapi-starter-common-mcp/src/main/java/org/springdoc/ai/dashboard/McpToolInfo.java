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

/**
 * DTO representing an MCP tool for the dashboard.
 *
 * @author bnasslahsen
 */
public class McpToolInfo {

	/**
	 * The tool name (operationId).
	 */
	private String name;

	/**
	 * The tool description.
	 */
	private String description;

	/**
	 * The JSON Schema for the tool input.
	 */
	private String inputSchema;

	/**
	 * The HTTP method.
	 */
	private String httpMethod;

	/**
	 * The API path.
	 */
	private String path;

	/**
	 * The tool group (e.g. OpenAPI tag name or "mcp-tools").
	 */
	private String group;

	/**
	 * Whether this tool uses a safe (read-only) HTTP method (GET, HEAD, OPTIONS).
	 */
	private boolean safe;

	/**
	 * Whether this tool requires human approval before execution.
	 */
	private boolean requiresApproval;

	/**
	 * Constructs a new McpToolInfo.
	 * @param name the tool name
	 * @param description the description
	 * @param inputSchema the input schema
	 * @param httpMethod the HTTP method
	 * @param path the API path
	 * @param group the tool group
	 */
	public McpToolInfo(String name, String description, String inputSchema, String httpMethod, String path,
			String group) {
		this.name = name;
		this.description = description;
		this.inputSchema = inputSchema;
		this.httpMethod = httpMethod;
		this.path = path;
		this.group = group;
	}

	/**
	 * Gets name.
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets name.
	 * @param name the name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets description.
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets description.
	 * @param description the description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Gets input schema.
	 * @return the input schema
	 */
	public String getInputSchema() {
		return inputSchema;
	}

	/**
	 * Sets input schema.
	 * @param inputSchema the input schema
	 */
	public void setInputSchema(String inputSchema) {
		this.inputSchema = inputSchema;
	}

	/**
	 * Gets http method.
	 * @return the http method
	 */
	public String getHttpMethod() {
		return httpMethod;
	}

	/**
	 * Sets http method.
	 * @param httpMethod the http method
	 */
	public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	/**
	 * Gets path.
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * Sets path.
	 * @param path the path
	 */
	public void setPath(String path) {
		this.path = path;
	}

	/**
	 * Gets group.
	 * @return the group
	 */
	public String getGroup() {
		return group;
	}

	/**
	 * Sets group.
	 * @param group the group
	 */
	public void setGroup(String group) {
		this.group = group;
	}

	/**
	 * Gets safe.
	 * @return true if the HTTP method is safe (read-only)
	 */
	public boolean isSafe() {
		return safe;
	}

	/**
	 * Sets safe.
	 * @param safe whether the tool uses a safe HTTP method
	 */
	public void setSafe(boolean safe) {
		this.safe = safe;
	}

	/**
	 * Gets requires approval.
	 * @return true if the tool requires human approval before execution
	 */
	public boolean isRequiresApproval() {
		return requiresApproval;
	}

	/**
	 * Sets requires approval.
	 * @param requiresApproval whether the tool requires human approval
	 */
	public void setRequiresApproval(boolean requiresApproval) {
		this.requiresApproval = requiresApproval;
	}

}
