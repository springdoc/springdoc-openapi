package org.springdoc.webflux.ui;

import java.util.List;

import org.springdoc.core.SwaggerUiConfigProperties;
import org.springdoc.ui.AbstractSwaggerResourceResolver;
import reactor.core.publisher.Mono;

import org.springframework.core.io.Resource;
import org.springframework.web.reactive.resource.ResourceResolver;
import org.springframework.web.reactive.resource.ResourceResolverChain;
import org.springframework.web.server.ServerWebExchange;

/**
 * The type Web jars version resource resolver.
 *
 * @author bnasslahsen
 */
public class SwaggerResourceResolver extends AbstractSwaggerResourceResolver implements ResourceResolver {


	/**
	 * Instantiates a new Web jars version resource resolver.
	 *
	 * @param swaggerUiConfigProperties the swagger ui config properties
	 */
	public SwaggerResourceResolver(SwaggerUiConfigProperties swaggerUiConfigProperties) {
		super(swaggerUiConfigProperties);
	}

	@Override
	public Mono<Resource> resolveResource(ServerWebExchange exchange, String requestPath, List<? extends Resource> locations, ResourceResolverChain chain) {
		return chain.resolveResource(exchange, requestPath, locations)
				.switchIfEmpty(Mono.defer(() -> {
					String webJarsResourcePath = findWebJarResourcePath(requestPath);
					if (webJarsResourcePath != null) {
						return chain.resolveResource(exchange, webJarsResourcePath, locations);
					}
					else {
						return Mono.empty();
					}
				}));
	}

	@Override
	public Mono<String> resolveUrlPath(String resourceUrlPath, List<? extends Resource> locations, ResourceResolverChain chain) {
		return chain.resolveUrlPath(resourceUrlPath, locations)
				.switchIfEmpty(Mono.defer(() -> {
					String webJarResourcePath = findWebJarResourcePath(resourceUrlPath);
					if (webJarResourcePath != null) {
						return chain.resolveUrlPath(webJarResourcePath, locations);
					}
					else {
						return Mono.empty();
					}
				}));
	}
}