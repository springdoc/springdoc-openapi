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

package org.springdoc.core.versions;

import java.util.List;

/**
 * Abstract base for delegating API version strategies that gracefully handle springdoc endpoint paths.
 * <p>
 * When path-based API versioning is configured (e.g., {@code usePathSegment(1)}),
 * the version resolver extracts a path segment from every request URI as the API version.
 * For springdoc endpoints like {@code /v3/api-docs}, this incorrectly extracts
 * {@code "api-docs"} as the version, causing an {@code InvalidApiVersionException}.
 * <p>
 * Subclasses wrap the platform-specific {@code ApiVersionStrategy} (servlet or reactive)
 * and use {@link #isSpringDocPath(String)} to detect springdoc paths before falling back
 * to the default version.
 *
 * @author bnasslahsen
 */
public abstract class AbstractSpringDocApiVersionStrategy {

	/**
	 * The springdoc path prefixes to protect from version resolution.
	 */
	private final List<String> springDocPaths;

	/**
	 * Instantiates a new abstract SpringDoc API version strategy.
	 *
	 * @param springDocPaths the springdoc path prefixes to protect
	 */
	protected AbstractSpringDocApiVersionStrategy(List<String> springDocPaths) {
		this.springDocPaths = springDocPaths;
	}

	/**
	 * Check if the given path targets a springdoc endpoint.
	 *
	 * @param path the request path (without context path)
	 * @return true if the path matches a springdoc endpoint prefix
	 */
	protected boolean isSpringDocPath(String path) {
		for (String springDocPath : springDocPaths) {
			if (path.startsWith(springDocPath)) {
				int len = springDocPath.length();
				if (path.length() == len || path.charAt(len) == '/' || path.charAt(len) == '.') {
					return true;
				}
			}
		}
		return false;
	}

}
