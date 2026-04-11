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

package org.springdoc.webflux.ai;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springdoc.ai.configuration.SpringDocAiAutoConfiguration;
import org.springdoc.ai.customizers.McpToolDescriptionCustomizer;
import org.springdoc.ai.properties.SpringDocAiProperties;
import org.springdoc.core.events.SpringDocAppInitializer;

import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.accept.ApiVersionStrategy;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;

import static org.springdoc.ai.properties.SpringDocAiProperties.SPRINGDOC_MCP_UI_ENABLED;

/**
 * Auto-configuration for MCP AI integration on WebFlux. Provides the dashboard WebFlux
 * configurer, reactive API version strategy wrapping, and dashboard initializer.
 *
 * @author bnasslahsen
 */
@Lazy(false)
@AutoConfiguration(after = SpringDocAiAutoConfiguration.class)
@ConditionalOnProperty(name = "springdoc.ai.mcp.enabled", matchIfMissing = true)
@ConditionalOnWebApplication(type = Type.REACTIVE)
public class McpWebFluxAiAutoConfiguration {

	/**
	 * Creates the {@link McpAuditMdcWebFilter} that populates MDC and
	 * {@link org.springdoc.ai.mcp.McpRequestContextHolder} for MCP requests.
	 * @param aiProperties the AI properties (used to scope the filter to the MCP path)
	 * @return the WebFilter bean
	 */
	@Bean
	@ConditionalOnMissingBean(McpAuditMdcWebFilter.class)
	McpAuditMdcWebFilter mcpAuditMdcWebFilter(SpringDocAiProperties aiProperties) {
		return new McpAuditMdcWebFilter(aiProperties.getMcpEndpoint());
	}

	/**
	 * Creates the {@link McpToolDescriptionCustomizer} bean that scans WebFlux handler
	 * methods for {@link org.springdoc.ai.annotations.McpToolDescription} annotations.
	 * @param requestMappingHandlerMapping the WebFlux request mapping handler mapping
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
	 * Creates the MCP Dashboard WebFlux configurer bean.
	 * @param aiProperties the AI properties
	 * @return the WebFlux configurer
	 */
	@Bean
	@ConditionalOnProperty(name = SPRINGDOC_MCP_UI_ENABLED, matchIfMissing = true)
	McpDashboardWebFluxConfigurer mcpDashboardWebFluxConfigurer(SpringDocAiProperties aiProperties) {
		return new McpDashboardWebFluxConfigurer(aiProperties);
	}

	/**
	 * Provides redirect routes for the MCP Dashboard path. Redirects both
	 * {@code /mcp-ui} and {@code /mcp-ui/} to {@code /mcp-ui/index.html}.
	 * @param aiProperties the AI properties
	 * @return the router function
	 */
	@Bean
	@ConditionalOnProperty(name = SPRINGDOC_MCP_UI_ENABLED, matchIfMissing = true)
	RouterFunction<ServerResponse> mcpDashboardRedirectRouter(SpringDocAiProperties aiProperties) {
		String dashboardPath = aiProperties.getDashboardPath();
		return RouterFunctions.route()
			.GET(dashboardPath,
					request -> ServerResponse.status(HttpStatus.FOUND)
						.location(URI.create(dashboardPath + "/index.html"))
						.build())
			.GET(dashboardPath + "/",
					request -> ServerResponse.status(HttpStatus.FOUND)
						.location(URI.create(dashboardPath + "/index.html"))
						.build())
			.build();
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
					mapping.setApiVersionStrategy(new McpReactiveApiVersionStrategy(original, dashboardPaths));
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
	 * Reactive-specific configuration that wraps the
	 * {@link org.springframework.web.reactive.accept.ApiVersionStrategy} on
	 * {@link org.springframework.web.reactive.function.server.support.RouterFunctionMapping}
	 * beans to gracefully handle MCP endpoint paths during API version resolution.
	 *
	 * @author bnasslahsen
	 */
	@Configuration(proxyBeanMethods = false)
	@ConditionalOnClass(name = "org.springframework.web.reactive.function.server.support.RouterFunctionMapping")
	static class McpReactiveApiVersionConfiguration {

		/**
		 * Wraps the {@link org.springframework.web.reactive.accept.ApiVersionStrategy} on
		 * all
		 * {@link org.springframework.web.reactive.function.server.support.RouterFunctionMapping}
		 * beans to prevent {@code InvalidApiVersionException} for the MCP endpoint.
		 * @param apiVersionStrategyOptional the api version strategy optional
		 * @param aiProperties the AI properties
		 * @param routerFunctionMappings the router function mappings
		 * @return the smart initializing singleton
		 */
		@Bean
		@Lazy(false)
		SmartInitializingSingleton mcpReactiveApiVersionCustomizer(
				Optional<org.springframework.web.reactive.accept.ApiVersionStrategy> apiVersionStrategyOptional,
				SpringDocAiProperties aiProperties,
				List<org.springframework.web.reactive.function.server.support.RouterFunctionMapping> routerFunctionMappings) {
			return () -> apiVersionStrategyOptional.ifPresent(strategy -> {
				List<String> mcpPaths = List.of(aiProperties.getMcpEndpoint(), aiProperties.getDashboardPath(),
						"/api/mcp-admin");
				for (org.springframework.web.reactive.function.server.support.RouterFunctionMapping mapping : routerFunctionMappings) {
					org.springframework.web.reactive.accept.ApiVersionStrategy original = mapping
						.getApiVersionStrategy();
					if (original != null) {
						mapping.setApiVersionStrategy(new McpReactiveApiVersionStrategy(original, mcpPaths));
					}
				}
			});
		}

	}

}
