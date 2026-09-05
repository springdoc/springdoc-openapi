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

import io.micrometer.context.ContextRegistry;
import org.slf4j.MDC;
import org.springdoc.ai.configuration.SpringDocAiAutoConfiguration;
import org.springdoc.ai.customizers.McpToolDescriptionCustomizer;
import org.springdoc.ai.properties.SpringDocAiProperties;
import org.springdoc.core.events.SpringDocAppInitializer;
import reactor.core.publisher.Hooks;

import org.springframework.beans.factory.InitializingBean;
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
@ConditionalOnProperty(name = "springdoc.ai.mcp.enabled", matchIfMissing = false)
@ConditionalOnWebApplication(type = Type.REACTIVE)
public class McpWebFluxAiAutoConfiguration {

	/**
	 * Creates the {@link McpAuditMdcWebFilter} that captures the MCP request context.
	 * @param aiProperties the AI properties (used to scope the filter to the MCP path)
	 * @return the WebFilter bean
	 */
	@Bean
	@ConditionalOnMissingBean(McpAuditMdcWebFilter.class)
	McpAuditMdcWebFilter mcpAuditMdcWebFilter(SpringDocAiProperties aiProperties) {
		return new McpAuditMdcWebFilter(aiProperties.getMcpEndpoint());
	}

	/**
	 * Wires the request context captured by {@link McpAuditMdcWebFilter} into the thread
	 * locals read by the synchronous audit logger and tool callback.
	 * <p>
	 * The filter writes the values into the Reactor context, which is per-subscription and
	 * therefore per-request. Registering the accessors below and enabling Reactor's
	 * automatic context propagation makes them visible as thread locals for the duration of
	 * each operator only, so the headers of one MCP request can no longer be observed while
	 * serving another on the same event-loop thread.
	 * @return an initializing bean that registers the accessors
	 */
	@Bean
	InitializingBean springDocMcpReactiveContextPropagation() {
		return () -> {
			ContextRegistry registry = ContextRegistry.getInstance();
			registry.registerThreadLocalAccessor(new McpRequestContextAccessor());
			registerMdcAccessor(registry, McpAuditMdcWebFilter.MDC_CLIENT_IP);
			registerMdcAccessor(registry, McpAuditMdcWebFilter.MDC_SESSION_ID);
			Hooks.enableAutomaticContextPropagation();
		};
	}

	/**
	 * Registers a thread-local accessor mirroring a single Reactor context entry into MDC.
	 * @param registry the Micrometer context registry
	 * @param key the shared context and MDC key
	 */
	private static void registerMdcAccessor(ContextRegistry registry, String key) {
		registry.registerThreadLocalAccessor(key, () -> MDC.get(key), value -> MDC.put(key, value),
				() -> MDC.remove(key));
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
	@ConditionalOnProperty(name = SPRINGDOC_MCP_UI_ENABLED, matchIfMissing = false)
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
	@ConditionalOnProperty(name = SPRINGDOC_MCP_UI_ENABLED, matchIfMissing = false)
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
	@ConditionalOnProperty(name = SPRINGDOC_MCP_UI_ENABLED, matchIfMissing = false)
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
	@ConditionalOnProperty(name = SPRINGDOC_MCP_UI_ENABLED, matchIfMissing = false)
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
