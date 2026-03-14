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

package org.springdoc.ai.configuration;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.ai.customizers.McpToolCustomizer;
import org.springdoc.ai.mcp.McpCommunityToolAuditAspect;
import org.springdoc.ai.mcp.McpNativeToolAuditAspect;
import org.springdoc.ai.mcp.OpenApiMcpToolCallbackProvider;
import org.springdoc.ai.properties.SpringDocAiProperties;
import org.springdoc.core.configuration.SpringDocConfiguration;
import org.springdoc.core.events.SpringDocAppInitializer;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.service.OpenAPIService;

import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import static org.springdoc.ai.properties.SpringDocAiProperties.SPRINGDOC_MCP_ENABLED;

/**
 * Auto-configuration for springdoc OpenAPI AI (MCP) integration.
 *
 * @author bnasslahsen
 */
@Lazy(false)
@AutoConfiguration(after = SpringDocConfiguration.class)
@ConditionalOnProperty(name = "springdoc.ai.mcp.enabled", matchIfMissing = true)
@ConditionalOnBean(SpringDocConfiguration.class)
public class SpringDocAiAutoConfiguration {

	/**
	 * Registers the audit aspect for Spring AI {@code @Tool}-annotated methods.
	 * @return the audit aspect
	 */
	@Bean
	@ConditionalOnMissingBean
	McpNativeToolAuditAspect mcpNativeToolAuditAspect() {
		return new McpNativeToolAuditAspect();
	}

	/**
	 * Registers the audit aspect for Spring AI Community {@code @McpTool}-annotated
	 * methods. Only active when the {@code org.springaicommunity.mcp.annotation.McpTool}
	 * class is on the classpath.
	 * @return the audit aspect
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnClass(name = "org.springaicommunity.mcp.annotation.McpTool")
	McpCommunityToolAuditAspect mcpCommunityToolAuditAspect() {
		return new McpCommunityToolAuditAspect();
	}

	/**
	 * Creates the OpenAPI MCP tool callback provider bean.
	 * @param openAPIService the OpenAPI service
	 * @param springDocConfigProperties the springdoc config properties
	 * @param aiProperties the AI properties
	 * @param mcpToolCustomizers the MCP tool customizers
	 * @return the tool callback provider
	 */
	@Bean
	@ConditionalOnMissingBean
	OpenApiMcpToolCallbackProvider openApiMcpToolCallbackProvider(OpenAPIService openAPIService,
			SpringDocConfigProperties springDocConfigProperties, SpringDocAiProperties aiProperties,
			Optional<List<McpToolCustomizer>> mcpToolCustomizers) {
		return new OpenApiMcpToolCallbackProvider(openAPIService, springDocConfigProperties, aiProperties,
				mcpToolCustomizers);
	}

	/**
	 * Creates the SpringDocAppInitializer for the MCP AI integration.
	 * @param aiProperties the AI properties
	 * @return the spring doc app initializer
	 */
	@Bean
	@ConditionalOnMissingBean(name = "springDocMcpInitializer")
	@Lazy(false)
	SpringDocAppInitializer springDocMcpInitializer(SpringDocAiProperties aiProperties) {
		return new SpringDocAppInitializer(aiProperties.getMcpEndpoint(), SPRINGDOC_MCP_ENABLED,
				aiProperties.isEnabled());
	}

	/**
	 * Deferred MCP tool registration. Runs after all singleton beans are created
	 * (including the MCP server), waits for the OpenAPI specification to become available
	 * in the cache, and dynamically registers tools with the {@code McpSyncServer}.
	 * <p>
	 * This avoids a startup deadlock: during bean creation, the pre-loading background
	 * thread is blocked by the singleton lock held by the main thread. Once all singletons
	 * are instantiated, the lock is released and the pre-loading thread completes
	 * immediately.
	 *
	 * @author bnasslahsen
	 */
	@Configuration(proxyBeanMethods = false)
	@ConditionalOnClass(name = { "io.modelcontextprotocol.server.McpSyncServer",
			"org.springframework.ai.mcp.McpToolUtils" })
	static class McpDeferredToolRegistration {

		/**
		 * The logger.
		 */
		private static final Logger LOGGER = LoggerFactory.getLogger(McpDeferredToolRegistration.class);

		/**
		 * The polling interval in milliseconds when waiting for the OpenAPI spec.
		 */
		private static final long POLL_INTERVAL_MS = 500;

		/**
		 * Registers MCP tools after all singletons are instantiated.
		 * @param toolCallbackProvider the tool callback provider
		 * @param openAPIService the OpenAPI service
		 * @param aiProperties the AI properties
		 * @param applicationContext the application context
		 * @return the smart initializing singleton
		 */
		@Bean
		SmartInitializingSingleton openApiMcpDeferredToolRegistrar(
				OpenApiMcpToolCallbackProvider toolCallbackProvider, OpenAPIService openAPIService,
				SpringDocAiProperties aiProperties, ApplicationContext applicationContext) {
			return () -> {
				io.swagger.v3.oas.models.OpenAPI openAPI = waitForOpenApi(openAPIService, aiProperties);
				if (openAPI == null) {
					LOGGER.warn("Timed out waiting for OpenAPI specification after {} seconds. "
							+ "No MCP tools will be registered.", aiProperties.getInitTimeoutSeconds());
					return;
				}
				ToolCallback[] tools = toolCallbackProvider.getToolCallbacks();
				if (tools.length == 0) {
					return;
				}
				try {
					io.modelcontextprotocol.server.McpSyncServer mcpServer = applicationContext
						.getBean(io.modelcontextprotocol.server.McpSyncServer.class);
					for (ToolCallback tool : tools) {
						mcpServer
							.addTool(org.springframework.ai.mcp.McpToolUtils.toSyncToolSpecification(tool));
					}
					mcpServer.notifyToolsListChanged();
					LOGGER.info("Registered {} MCP tools with the MCP server", tools.length);
				}
				catch (org.springframework.beans.factory.NoSuchBeanDefinitionException ex) {
					LOGGER.debug("McpSyncServer bean not found. Tools available via ToolCallbackProvider.");
				}
			};
		}

		/**
		 * Polls the OpenAPI cache until the specification is available or the timeout
		 * expires.
		 * @param openAPIService the OpenAPI service
		 * @param aiProperties the AI properties
		 * @return the OpenAPI spec, or null if not available within the timeout
		 */
		private io.swagger.v3.oas.models.OpenAPI waitForOpenApi(OpenAPIService openAPIService,
				SpringDocAiProperties aiProperties) {
			int timeoutSeconds = aiProperties.getInitTimeoutSeconds();
			long deadline = System.currentTimeMillis() + (timeoutSeconds * 1000L);
			while (System.currentTimeMillis() < deadline) {
				io.swagger.v3.oas.models.OpenAPI openAPI = openAPIService
					.getCachedOpenAPI(Locale.getDefault());
				if (openAPI != null) {
					return openAPI;
				}
				try {
					Thread.sleep(POLL_INTERVAL_MS);
				}
				catch (InterruptedException ex) {
					Thread.currentThread().interrupt();
					return null;
				}
			}
			return null;
		}

	}

}
