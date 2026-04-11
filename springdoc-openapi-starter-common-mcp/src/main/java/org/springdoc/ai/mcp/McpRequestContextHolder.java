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

package org.springdoc.ai.mcp;

import java.util.Map;
import java.util.Set;

/**
 * Thread-local holder for HTTP headers captured from inbound MCP requests. Platform-specific
 * filters ({@code McpAuditMdcFilter} for WebMVC, {@code McpAuditMdcWebFilter} for WebFlux)
 * populate this holder so that {@link OpenApiToolCallback} can propagate headers
 * (e.g. {@code Authorization}) to downstream REST API calls.
 *
 * <p>Uses a plain {@link ThreadLocal} (not {@link InheritableThreadLocal}) to avoid
 * leaking credentials to child threads.
 *
 * @author bnasslahsen
 */
public final class McpRequestContextHolder {

	/**
	 * Headers to skip when forwarding from inbound requests to downstream API calls.
	 * These are standard HTTP transport/browser headers that should not be propagated.
	 */
	public static final Set<String> SKIP_HEADERS = Set.of("content-type", "content-length", "host", "connection",
			"accept", "accept-encoding", "accept-language", "user-agent", "origin", "referer", "cookie",
			"sec-fetch-dest", "sec-fetch-mode", "sec-fetch-site", "sec-ch-ua", "sec-ch-ua-mobile",
			"sec-ch-ua-platform");

	private static final ThreadLocal<Map<String, String>> HEADERS = new ThreadLocal<>();

	private McpRequestContextHolder() {
	}

	/**
	 * Stores the forwardable headers from the current MCP request.
	 * @param headers the filtered headers to propagate
	 */
	public static void setHeaders(Map<String, String> headers) {
		HEADERS.set(headers);
	}

	/**
	 * Returns the forwardable headers captured from the current MCP request,
	 * or {@code null} if none were set.
	 * @return the captured headers, or {@code null}
	 */
	public static Map<String, String> getHeaders() {
		return HEADERS.get();
	}

	/**
	 * Removes the stored headers. Must be called in a {@code finally} block to
	 * prevent leaking values across thread-pool reuse.
	 */
	public static void clear() {
		HEADERS.remove();
	}

}
