/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2025 the original author or authors.
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

package org.springdoc.webflux.api;

import java.util.List;

import org.jspecify.annotations.Nullable;
import org.springdoc.core.versions.AbstractSpringDocApiVersionStrategy;
import reactor.core.publisher.Mono;

import org.springframework.web.accept.InvalidApiVersionException;
import org.springframework.web.reactive.accept.ApiVersionStrategy;
import org.springframework.web.server.ServerWebExchange;

/**
 * Reactive delegating {@link ApiVersionStrategy} that gracefully handles springdoc endpoint paths.
 *
 * @author bnasslahsen
 * @see AbstractSpringDocApiVersionStrategy
 */
public class SpringDocApiVersionStrategy extends AbstractSpringDocApiVersionStrategy implements ApiVersionStrategy {

	/**
	 * The delegate strategy.
	 */
	private final ApiVersionStrategy delegate;

	/**
	 * Instantiates a new SpringDoc API version strategy.
	 *
	 * @param delegate the delegate strategy
	 * @param springDocPaths the springdoc path prefixes to protect
	 */
	public SpringDocApiVersionStrategy(ApiVersionStrategy delegate, List<String> springDocPaths) {
		super(springDocPaths);
		this.delegate = delegate;
	}

	@Override
	public @Nullable Comparable<?> resolveParseAndValidateVersion(ServerWebExchange exchange) {
		try {
			return delegate.resolveParseAndValidateVersion(exchange);
		}
		catch (InvalidApiVersionException ex) {
			if (isSpringDocPath(exchange)) {
				return delegate.getDefaultVersion();
			}
			throw ex;
		}
	}

	@Override
	public Mono<Comparable<?>> resolveParseAndValidateApiVersion(ServerWebExchange exchange) {
		return delegate.resolveParseAndValidateApiVersion(exchange)
				.onErrorResume(InvalidApiVersionException.class, ex -> {
					if (isSpringDocPath(exchange)) {
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
	 * Check if the request targets a springdoc endpoint path.
	 *
	 * @param exchange the server web exchange
	 * @return true if the request is for a springdoc endpoint
	 */
	private boolean isSpringDocPath(ServerWebExchange exchange) {
		String path = exchange.getRequest().getPath().pathWithinApplication().value();
		return isSpringDocPath(path);
	}

}
