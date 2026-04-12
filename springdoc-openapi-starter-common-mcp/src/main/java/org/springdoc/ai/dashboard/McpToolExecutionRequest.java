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
 * Request DTO for executing an MCP tool.
 *
 * @author bnasslahsen
 */
public class McpToolExecutionRequest {

	/**
	 * The tool name to execute.
	 */
	private String toolName;

	/**
	 * The JSON arguments string.
	 */
	private String arguments;

	/**
	 * Whether human approval has been granted for this execution.
	 */
	private boolean approved;

	/**
	 * Gets tool name.
	 * @return the tool name
	 */
	public String getToolName() {
		return toolName;
	}

	/**
	 * Sets tool name.
	 * @param toolName the tool name
	 */
	public void setToolName(String toolName) {
		this.toolName = toolName;
	}

	/**
	 * Gets arguments.
	 * @return the arguments
	 */
	public String getArguments() {
		return arguments;
	}

	/**
	 * Sets arguments.
	 * @param arguments the arguments
	 */
	public void setArguments(String arguments) {
		this.arguments = arguments;
	}

	/**
	 * Returns whether human approval has been granted.
	 * @return true if approved
	 */
	public boolean isApproved() {
		return approved;
	}

	/**
	 * Sets whether human approval has been granted.
	 * @param approved true if approved
	 */
	public void setApproved(boolean approved) {
		this.approved = approved;
	}

}
