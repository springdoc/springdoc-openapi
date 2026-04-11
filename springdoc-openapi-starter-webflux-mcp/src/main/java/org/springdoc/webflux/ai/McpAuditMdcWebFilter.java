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

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.MDC;
import org.springdoc.ai.mcp.McpRequestContextHolder;
import reactor.core.publisher.Mono;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

/**
 * Reactive WebFilter that populates SLF4J MDC with the caller's IP address and MCP session
 * ID, and stores forwardable headers in {@link McpRequestContextHolder} for propagation
 * to downstream REST API calls.
 *
 * <p>This is the WebFlux equivalent of
 * {@code org.springdoc.webmvc.ai.McpAuditMdcFilter}.
 *
 * @author bnasslahsen
 */
public class McpAuditMdcWebFilter implements WebFilter {

	/**
	 * MDC key used by {@link org.springdoc.ai.mcp.McpAuditLogger} to read the client IP
	 * address.
	 */
	private static final String MDC_CLIENT_IP = "clientIp";

	/**
	 * MDC key used by {@link org.springdoc.ai.mcp.McpAuditLogger} to read the MCP
	 * session ID.
	 */
	private static final String MDC_SESSION_ID = "sessionId";

	/**
	 * HTTP header set by reverse proxies carrying the originating client IP.
	 */
	private static final String HEADER_X_FORWARDED_FOR = "X-Forwarded-For";

	/**
	 * MCP Streamable HTTP transport header carrying the session identifier.
	 */
	private static final String HEADER_MCP_SESSION_ID = "mcp-session-id";

	/**
	 * The MCP endpoint path to match against.
	 */
	private final String mcpEndpoint;

	/**
	 * Creates a new filter scoped to the given MCP endpoint path.
	 * @param mcpEndpoint the MCP endpoint path (e.g. {@code /mcp})
	 */
	public McpAuditMdcWebFilter(String mcpEndpoint) {
		this.mcpEndpoint = mcpEndpoint;
	}

	@Override
	public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
		String path = exchange.getRequest().getPath().value();
		if (!path.startsWith(mcpEndpoint)) {
			return chain.filter(exchange);
		}
		ServerHttpRequest request = exchange.getRequest();
		MDC.put(MDC_CLIENT_IP, resolveClientIp(request));
		String sessionId = request.getHeaders().getFirst(HEADER_MCP_SESSION_ID);
		if (sessionId != null && !sessionId.isBlank()) {
			MDC.put(MDC_SESSION_ID, sessionId);
		}
		McpRequestContextHolder.setHeaders(extractForwardableHeaders(request.getHeaders()));
		return chain.filter(exchange)
				.doFinally(signal -> {
					MDC.remove(MDC_CLIENT_IP);
					MDC.remove(MDC_SESSION_ID);
					McpRequestContextHolder.clear();
				});
	}

	/**
	 * Extracts headers that should be forwarded to downstream REST API calls,
	 * filtering out standard HTTP transport/browser headers.
	 * @param httpHeaders the reactive request headers
	 * @return the forwardable headers map
	 */
	private Map<String, String> extractForwardableHeaders(HttpHeaders httpHeaders) {
		Map<String, String> headers = new HashMap<>();
		httpHeaders.forEach((name, values) -> {
			if (!McpRequestContextHolder.SKIP_HEADERS.contains(name.toLowerCase()) && !values.isEmpty()) {
				String value = values.get(0);
				if (value != null && !value.isEmpty()) {
					headers.put(name, value);
				}
			}
		});
		return headers;
	}

	/**
	 * Resolves the originating client IP from the request. Prefers the first value in
	 * {@code X-Forwarded-For} when set, otherwise falls back to the remote address.
	 * @param request the reactive server request
	 * @return the resolved client IP address string
	 */
	private String resolveClientIp(ServerHttpRequest request) {
		String forwarded = request.getHeaders().getFirst(HEADER_X_FORWARDED_FOR);
		if (forwarded != null && !forwarded.isBlank()) {
			int comma = forwarded.indexOf(',');
			return (comma >= 0 ? forwarded.substring(0, comma) : forwarded).strip();
		}
		InetSocketAddress remoteAddress = request.getRemoteAddress();
		if (remoteAddress != null) {
			InetAddress address = remoteAddress.getAddress();
			return address != null ? address.getHostAddress() : remoteAddress.getHostString();
		}
		return "unknown";
	}

}
