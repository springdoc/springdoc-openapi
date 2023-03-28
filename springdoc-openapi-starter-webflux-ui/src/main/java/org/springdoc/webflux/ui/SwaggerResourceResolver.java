package org.springdoc.webflux.ui;

import java.util.List;

import org.springdoc.core.properties.SwaggerUiConfigProperties;
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
		Mono<Resource> resolved = chain.resolveResource(exchange, requestPath, locations);
		if (!Mono.empty().equals(resolved)) {
			String webJarResourcePath = findWebJarResourcePath(requestPath);
			if (webJarResourcePath != null)
				return chain.resolveResource(exchange, webJarResourcePath, locations);
		}
		return resolved;
	}

	@Override
	public Mono<String> resolveUrlPath(String resourcePath, List<? extends Resource> locations, ResourceResolverChain chain) {
		Mono<String> path = chain.resolveUrlPath(resourcePath, locations);
		if (!Mono.empty().equals(path)) {
			String webJarResourcePath = findWebJarResourcePath(resourcePath);
			if (webJarResourcePath != null)
				return chain.resolveUrlPath(webJarResourcePath, locations);
		}
		return path;
	}
}