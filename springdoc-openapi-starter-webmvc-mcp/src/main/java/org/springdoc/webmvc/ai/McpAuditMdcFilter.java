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

package org.springdoc.webmvc.ai;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;

import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Servlet filter that populates SLF4J MDC with the caller's IP address and MCP session
 * ID for each request to the MCP endpoint. This enables
 * {@link org.springdoc.ai.mcp.McpAuditLogger} to include {@code client_ip} and
 * {@code session_id} in every audit event without requiring any changes to individual
 * tool callbacks.
 *
 * <p>
 * The client IP is resolved from the {@code X-Forwarded-For} header (first entry, to
 * handle proxies) with fallback to {@code HttpServletRequest.getRemoteAddr()}.
 *
 * <p>
 * The MCP session ID is read from the {@code mcp-session-id} request header, as defined
 * by the MCP Streamable HTTP transport specification.
 *
 * <p>
 * MDC entries are always cleared after the request completes to prevent leaking values
 * across thread-pool reuse.
 *
 * @author bnasslahsen
 */
public class McpAuditMdcFilter extends OncePerRequestFilter {

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
	 * HTTP header set by reverse proxies carrying the originating client IP. When
	 * multiple values are present (proxy chain), only the first (leftmost) is used.
	 */
	private static final String HEADER_X_FORWARDED_FOR = "X-Forwarded-For";

	/**
	 * MCP Streamable HTTP transport header carrying the session identifier.
	 */
	private static final String HEADER_MCP_SESSION_ID = "mcp-session-id";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		MDC.put(MDC_CLIENT_IP, resolveClientIp(request));
		String sessionId = request.getHeader(HEADER_MCP_SESSION_ID);
		if (sessionId != null && !sessionId.isBlank()) {
			MDC.put(MDC_SESSION_ID, sessionId);
		}
		try {
			filterChain.doFilter(request, response);
		}
		finally {
			MDC.remove(MDC_CLIENT_IP);
			MDC.remove(MDC_SESSION_ID);
		}
	}

	/**
	 * Resolves the originating client IP from the request. Prefers the first value in
	 * {@code X-Forwarded-For} when set, otherwise falls back to the direct remote
	 * address.
	 * @param request the HTTP servlet request
	 * @return the resolved client IP address string
	 */
	private String resolveClientIp(HttpServletRequest request) {
		String forwarded = request.getHeader(HEADER_X_FORWARDED_FOR);
		if (forwarded != null && !forwarded.isBlank()) {
			int comma = forwarded.indexOf(',');
			return (comma >= 0 ? forwarded.substring(0, comma) : forwarded).strip();
		}
		return request.getRemoteAddr();
	}

}
