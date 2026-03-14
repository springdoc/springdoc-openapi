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

import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springdoc.core.versions.AbstractSpringDocApiVersionStrategy;
import reactor.core.publisher.Mono;

import org.springframework.web.accept.InvalidApiVersionException;
import org.springframework.web.reactive.accept.ApiVersionStrategy;
import org.springframework.web.server.ServerWebExchange;

/**
 * Reactive delegating {@link ApiVersionStrategy} that gracefully handles MCP endpoint
 * paths during API version resolution.
 * <p>
 * When path-based API versioning is configured (e.g., {@code usePathSegment(1)}), the
 * version resolver tries to extract a version from every request URI, including the MCP
 * endpoint ({@code /mcp}). This causes an {@code InvalidApiVersionException} because the
 * MCP path does not contain enough segments.
 * <p>
 * This strategy wraps the original and catches the exception for MCP paths, returning the
 * default version instead.
 *
 * @author bnasslahsen
 * @see AbstractSpringDocApiVersionStrategy
 */
public class McpReactiveApiVersionStrategy extends AbstractSpringDocApiVersionStrategy implements ApiVersionStrategy {

	/**
	 * The delegate strategy.
	 */
	private final ApiVersionStrategy delegate;

	/**
	 * Instantiates a new MCP reactive API version strategy.
	 * @param delegate the delegate strategy
	 * @param mcpPaths the MCP path prefixes to protect
	 */
	public McpReactiveApiVersionStrategy(ApiVersionStrategy delegate, List<String> mcpPaths) {
		super(mcpPaths);
		this.delegate = delegate;
	}

	@Override
	public @Nullable Comparable<?> resolveParseAndValidateVersion(ServerWebExchange exchange) {
		try {
			return delegate.resolveParseAndValidateVersion(exchange);
		}
		catch (InvalidApiVersionException ex) {
			if (isMcpPath(exchange)) {
				return delegate.getDefaultVersion();
			}
			throw ex;
		}
	}

	@Override
	public Mono<Comparable<?>> resolveParseAndValidateApiVersion(ServerWebExchange exchange) {
		return delegate.resolveParseAndValidateApiVersion(exchange)
			.onErrorResume(InvalidApiVersionException.class, ex -> {
				if (isMcpPath(exchange)) {
					Comparable<?> defaultVersion = delegate.getDefaultVersion();
					return defaultVersion != null ? Mono.just(defaultVersion) : Mono.empty();
				}
				return Mono.error(ex);
			});
	}

	@Override
	public @Nullable String resolveVersion(ServerWebExchange exchange) {
		return delegate.resolveVersion(exchange);
	}

	@Override
	public Comparable<?> parseVersion(String version) {
		return delegate.parseVersion(version);
	}

	@Override
	public void validateVersion(@Nullable Comparable<?> requestVersion, ServerWebExchange exchange) {
		delegate.validateVersion(requestVersion, exchange);
	}

	@Override
	public @Nullable Comparable<?> getDefaultVersion() {
		return delegate.getDefaultVersion();
	}

	@Override
	public void handleDeprecations(Comparable<?> version, Object handler, ServerWebExchange exchange) {
		delegate.handleDeprecations(version, handler, exchange);
	}

	/**
	 * Check if the request targets an MCP endpoint path.
	 * @param exchange the server web exchange
	 * @return true if the request is for an MCP endpoint
	 */
	private boolean isMcpPath(ServerWebExchange exchange) {
		String path = exchange.getRequest().getPath().pathWithinApplication().value();
		return isSpringDocPath(path);
	}

}
