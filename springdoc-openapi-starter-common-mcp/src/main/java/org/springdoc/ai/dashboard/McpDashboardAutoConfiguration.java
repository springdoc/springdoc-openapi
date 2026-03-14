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

import org.springdoc.ai.configuration.SpringDocAiAutoConfiguration;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import static org.springdoc.ai.properties.SpringDocAiProperties.SPRINGDOC_MCP_UI_ENABLED;

/**
 * Auto-configuration for the MCP Developer Dashboard.
 *
 * @author bnasslahsen
 */
@Lazy(false)
@AutoConfiguration(after = SpringDocAiAutoConfiguration.class)
@ConditionalOnProperty(name = "springdoc.ai.mcp.enabled", matchIfMissing = true)
public class McpDashboardAutoConfiguration {

	/**
	 * Creates the in-memory audit event store. Always registered when the dashboard is
	 * enabled so that audit events are captured even before the first UI request.
	 * @return the audit event store
	 */
	@Bean
	@ConditionalOnProperty(name = SPRINGDOC_MCP_UI_ENABLED, matchIfMissing = true)
	McpAuditEventStore mcpAuditEventStore() {
		return new McpAuditEventStore();
	}

	/**
	 * Creates the dashboard tool source backed by Spring AI ToolCallbackProvider beans.
	 * @param toolCallbackProviders the tool callback providers
	 * @return the tool callback dashboard tool source
	 */
	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	@ConditionalOnProperty(name = SPRINGDOC_MCP_UI_ENABLED, matchIfMissing = true)
	ToolCallbackDashboardToolSource toolCallbackDashboardToolSource(
			List<org.springframework.ai.tool.ToolCallbackProvider> toolCallbackProviders) {
		return new ToolCallbackDashboardToolSource(toolCallbackProviders);
	}

	/**
	 * Creates the MCP Dashboard controller bean.
	 * @param toolSources the dashboard tool sources
	 * @param auditEventStore the in-memory audit event store
	 * @return the dashboard controller
	 */
	@Bean
	@ConditionalOnProperty(name = SPRINGDOC_MCP_UI_ENABLED, matchIfMissing = true)
	McpDashboardController mcpDashboardController(List<McpDashboardToolSource> toolSources,
			McpAuditEventStore auditEventStore) {
		return new McpDashboardController(toolSources, auditEventStore);
	}

	/**
	 * Conditional configuration for MCP server tool discovery. Only active when the MCP
	 * SDK is on the classpath (i.e., when the application is an MCP server with
	 * {@code @McpTool} annotated methods).
	 *
	 * @author bnasslahsen
	 */
	@Configuration(proxyBeanMethods = false)
	@ConditionalOnClass(name = "io.modelcontextprotocol.server.McpServerFeatures")
	@ConditionalOnProperty(name = SPRINGDOC_MCP_UI_ENABLED, matchIfMissing = true)
	static class McpServerToolDiscoveryConfiguration {

		/**
		 * Creates a dashboard tool source from MCP SyncToolSpecification beans.
		 * @param toolSpecifications the sync tool specification lists from the context
		 * @return the MCP server dashboard tool source
		 */
		@Bean
		@Order(Ordered.LOWEST_PRECEDENCE)
		McpSyncServerDashboardToolSource mcpSyncServerDashboardToolSource(
				ObjectProvider<List<io.modelcontextprotocol.server.McpServerFeatures.SyncToolSpecification>> toolSpecifications,
				org.springframework.context.ApplicationContext applicationContext) {
			return new McpSyncServerDashboardToolSource(toolSpecifications.stream().toList(), applicationContext);
		}

	}

}
