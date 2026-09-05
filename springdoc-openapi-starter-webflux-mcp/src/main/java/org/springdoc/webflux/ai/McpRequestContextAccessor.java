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

import java.util.Map;

import io.micrometer.context.ThreadLocalAccessor;
import org.springdoc.ai.mcp.McpRequestContextHolder;

/**
 * Bridges the Reactor context entry written by {@link McpAuditMdcWebFilter} to the
 * {@link McpRequestContextHolder} thread local, so that the synchronous tool execution in
 * {@code OpenApiToolCallback} reads the headers of the request it actually belongs to.
 *
 * <p>Registered with the Micrometer {@code ContextRegistry} by
 * {@link McpWebFluxAiAutoConfiguration}; Reactor's automatic context propagation then sets
 * and restores the thread local around each operator, whichever event-loop thread runs it.
 *
 * @author bnasslahsen
 */
public class McpRequestContextAccessor implements ThreadLocalAccessor<Map<String, String>> {

	@Override
	public Object key() {
		return McpRequestContextHolder.CONTEXT_KEY;
	}

	@Override
	public Map<String, String> getValue() {
		return McpRequestContextHolder.getHeaders();
	}

	@Override
	public void setValue(Map<String, String> value) {
		McpRequestContextHolder.setHeaders(value);
	}

	@Override
	public void setValue() {
		McpRequestContextHolder.clear();
	}

}
