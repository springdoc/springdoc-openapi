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

import java.util.List;
import java.util.Map;

/**
 * Abstraction for dashboard tool sources. Allows the dashboard to discover and execute
 * tools from different providers (OpenAPI-based tools, MCP annotation tools, etc.)
 *
 * @author bnasslahsen
 */
public interface McpDashboardToolSource {

	/**
	 * Returns the list of available tools from this source.
	 * @return the tool info list
	 */
	List<McpToolInfo> getToolInfos();

	/**
	 * Executes a tool by name. Returns {@code null} if this source does not contain a
	 * tool with the given name.
	 * @param toolName the tool name
	 * @param arguments the JSON arguments string
	 * @param extraHeaders extra HTTP headers to forward (e.g. Authorization)
	 * @return the execution response, or null if tool not found in this source
	 */
	McpToolExecutionResponse executeTool(String toolName, String arguments, Map<String, String> extraHeaders);

}
