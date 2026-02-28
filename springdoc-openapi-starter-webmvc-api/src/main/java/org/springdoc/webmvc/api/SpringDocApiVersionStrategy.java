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

package org.springdoc.webmvc.api;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.Nullable;
import org.springdoc.core.versions.AbstractSpringDocApiVersionStrategy;

import org.springframework.web.accept.ApiVersionStrategy;
import org.springframework.web.accept.InvalidApiVersionException;

/**
 * Servlet-based delegating {@link ApiVersionStrategy} that gracefully handles springdoc endpoint paths.
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
	public @Nullable Comparable<?> resolveParseAndValidateVersion(HttpServletRequest request) {
		try {
			return delegate.resolveParseAndValidateVersion(request);
		}
		catch (InvalidApiVersionException ex) {
			if (isSpringDocPath(request)) {
				return delegate.getDefaultVersion();
			}
			throw ex;
		}
	}

	@Override
	public @Nullable String resolveVersion(HttpServletRequest request) {
		return delegate.resolveVersion(request);
	}

	@Override
	public Comparable<?> parseVersion(String version) {
		return delegate.parseVersion(version);
	}

	@Override
	public void validateVersion(@Nullable Comparable<?> requestVersion, HttpServletRequest request) {
		delegate.validateVersion(requestVersion, request);
	}

	@Override
	public @Nullable Comparable<?> getDefaultVersion() {
		return delegate.getDefaultVersion();
	}

	@Override
	public void handleDeprecations(Comparable<?> version, Object handler,
			HttpServletRequest request, HttpServletResponse response) {
		delegate.handleDeprecations(version, handler, request, response);
	}

	/**
	 * Check if the request targets a springdoc endpoint path.
	 *
	 * @param request the HTTP request
	 * @return true if the request is for a springdoc endpoint
	 */
	private boolean isSpringDocPath(HttpServletRequest request) {
		String requestURI = request.getRequestURI();
		String contextPath = request.getContextPath();
		String path = requestURI;
		if (contextPath != null && !contextPath.isEmpty()) {
			path = requestURI.substring(contextPath.length());
		}
		return isSpringDocPath(path);
	}

}
