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

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;

/**
 * Implement and register a bean of type {@link McpToolCustomizer} to customize the
 * MCP tool definition (name, description, input schema) before it is registered.
 * Multiple customizers are applied in order. Return {@code null} from
 * {@link #customize} to exclude the tool entirely.
 *
 * @author bnasslahsen
 */
@FunctionalInterface
public interface McpToolCustomizer {

	/**
	 * Customizes the tool definition context for a single OpenAPI operation.
	 * @param context the mutable tool definition context
	 * @param path the OpenAPI path (e.g. {@code /users/&#123;id&#125;})
	 * @param method the HTTP method
	 * @param operation the OpenAPI operation
	 * @return the modified context, or {@code null} to exclude the tool
	 */
	McpToolDefinitionContext customize(McpToolDefinitionContext context, String path, PathItem.HttpMethod method,
			Operation operation);

}
