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

package org.springdoc.webmvc.ai;

import java.util.List;
import java.util.Optional;

import org.springdoc.ai.customizers.McpToolDescriptionCustomizer;
import org.springdoc.ai.dashboard.McpDashboardAutoConfiguration;
import org.springdoc.ai.properties.SpringDocAiProperties;
import org.springdoc.core.events.SpringDocAppInitializer;

import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.accept.ApiVersionStrategy;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import static org.springdoc.ai.properties.SpringDocAiProperties.SPRINGDOC_MCP_UI_ENABLED;

/**
 * Auto-configuration for MCP AI integration on WebMvc. Provides the dashboard WebMvc
 * configurer, servlet API version strategy wrapping, and dashboard initializer.
 *
 * @author bnasslahsen
 */
@Lazy(false)
@AutoConfiguration(after = McpDashboardAutoConfiguration.class)
@ConditionalOnProperty(name = "springdoc.ai.mcp.enabled", matchIfMissing = true)
@ConditionalOnWebApplication(type = Type.SERVLET)
public class McpWebMvcAiAutoConfiguration {

	/**
	 * Registers a servlet filter that populates SLF4J MDC with the client IP address and
	 * MCP session ID for every request to the MCP endpoint, making them available to
	 * {@link org.springdoc.ai.mcp.McpAuditLogger} without any changes to tool callbacks.
	 * @param aiProperties the AI properties (used to scope the filter to the MCP path)
	 * @return the filter registration bean
	 */
	@Bean
	@ConditionalOnMissingBean(McpAuditMdcFilter.class)
	FilterRegistrationBean<McpAuditMdcFilter> mcpAuditMdcFilter(SpringDocAiProperties aiProperties) {
		FilterRegistrationBean<McpAuditMdcFilter> registration = new FilterRegistrationBean<>(new McpAuditMdcFilter());
		registration.addUrlPatterns(aiProperties.getMcpEndpoint() + "/*", aiProperties.getMcpEndpoint());
		registration.setOrder(1);
		registration.setName("mcpAuditMdcFilter");
		return registration;
	}

	/**
	 * Creates the {@link McpToolDescriptionCustomizer} bean that scans WebMvc handler
	 * methods for {@link org.springdoc.ai.annotations.McpToolDescription} annotations.
	 * @param requestMappingHandlerMapping the WebMvc request mapping handler mapping
	 * @return the MCP tool description customizer
	 */
	@Bean
	@ConditionalOnMissingBean
	McpToolDescriptionCustomizer mcpToolDescriptionCustomizer(
			RequestMappingHandlerMapping requestMappingHandlerMapping) {
		var handlerMethods = requestMappingHandlerMapping.getHandlerMethods().values();
		return new McpToolDescriptionCustomizer(
				McpToolDescriptionCustomizer.buildAnnotationMap(handlerMethods),
				McpToolDescriptionCustomizer.buildExcludedOperationIds(handlerMethods));
	}

	/**
	 * Creates the MCP Dashboard WebMvc configurer bean.
	 * @param aiProperties the AI properties
	 * @return the WebMvc configurer
	 */
	@Bean
	@ConditionalOnProperty(name = SPRINGDOC_MCP_UI_ENABLED, matchIfMissing = true)
	McpDashboardWebMvcConfigurer mcpDashboardWebMvcConfigurer(SpringDocAiProperties aiProperties) {
		return new McpDashboardWebMvcConfigurer(aiProperties);
	}

	/**
	 * Wraps the {@link ApiVersionStrategy} on all {@link RequestMappingHandlerMapping}
	 * beans to prevent {@code InvalidApiVersionException} for dashboard API paths.
	 * @param apiVersionStrategyOptional the api version strategy optional
	 * @param aiProperties the AI properties
	 * @param handlerMappings the request mapping handler mappings
	 * @return the smart initializing singleton
	 */
	@Bean
	@Lazy(false)
	@ConditionalOnProperty(name = SPRINGDOC_MCP_UI_ENABLED, matchIfMissing = true)
	SmartInitializingSingleton mcpDashboardApiVersionCustomizer(Optional<ApiVersionStrategy> apiVersionStrategyOptional,
			SpringDocAiProperties aiProperties, List<RequestMappingHandlerMapping> handlerMappings) {
		return () -> apiVersionStrategyOptional.ifPresent(strategy -> {
			List<String> dashboardPaths = List.of("/api/mcp-admin", aiProperties.getDashboardPath());
			for (RequestMappingHandlerMapping mapping : handlerMappings) {
				ApiVersionStrategy original = mapping.getApiVersionStrategy();
				if (original != null) {
					mapping.setApiVersionStrategy(new McpApiVersionStrategy(original, dashboardPaths));
				}
			}
		});
	}

	/**
	 * Creates the SpringDocAppInitializer for the MCP Dashboard UI.
	 * @param aiProperties the AI properties
	 * @return the spring doc app initializer
	 */
	@Bean
	@ConditionalOnMissingBean(name = "springDocMcpDashboardInitializer")
	@ConditionalOnProperty(name = SPRINGDOC_MCP_UI_ENABLED, matchIfMissing = true)
	@Lazy(false)
	SpringDocAppInitializer springDocMcpDashboardInitializer(SpringDocAiProperties aiProperties) {
		return new SpringDocAppInitializer(aiProperties.getDashboardPath(), SPRINGDOC_MCP_UI_ENABLED,
				aiProperties.isDashboardEnabled());
	}

	/**
	 * Servlet-specific configuration that wraps the
	 * {@link org.springframework.web.accept.ApiVersionStrategy} on
	 * {@link org.springframework.web.servlet.function.support.RouterFunctionMapping}
	 * beans to gracefully handle MCP endpoint paths during API version resolution.
	 *
	 * @author bnasslahsen
	 */
	@Configuration(proxyBeanMethods = false)
	@ConditionalOnClass(name = "org.springframework.web.servlet.function.support.RouterFunctionMapping")
	static class McpServletApiVersionConfiguration {

		/**
		 * Wraps the {@link org.springframework.web.accept.ApiVersionStrategy} on all
		 * {@link org.springframework.web.servlet.function.support.RouterFunctionMapping}
		 * beans to prevent {@code InvalidApiVersionException} for the MCP endpoint.
		 * @param apiVersionStrategyOptional the api version strategy optional
		 * @param aiProperties the AI properties
		 * @param routerFunctionMappings the router function mappings
		 * @return the smart initializing singleton
		 */
		@Bean
		@Lazy(false)
		SmartInitializingSingleton mcpServletApiVersionCustomizer(
				Optional<org.springframework.web.accept.ApiVersionStrategy> apiVersionStrategyOptional,
				SpringDocAiProperties aiProperties,
				List<org.springframework.web.servlet.function.support.RouterFunctionMapping> routerFunctionMappings) {
			return () -> apiVersionStrategyOptional.ifPresent(strategy -> {
				List<String> mcpPaths = List.of(aiProperties.getMcpEndpoint(), aiProperties.getDashboardPath(),
						"/api/mcp-admin");
				for (org.springframework.web.servlet.function.support.RouterFunctionMapping mapping : routerFunctionMappings) {
					org.springframework.web.accept.ApiVersionStrategy original = mapping.getApiVersionStrategy();
					if (original != null) {
						mapping.setApiVersionStrategy(new McpApiVersionStrategy(original, mcpPaths));
					}
				}
			});
		}

	}

}
