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

package test.org.springdoc.ai.mcp.guardrails;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import io.swagger.v3.oas.models.PathItem;
import org.junit.jupiter.api.Test;
import org.springdoc.ai.customizers.McpToolCustomizer;
import org.springdoc.ai.mcp.OpenApiMcpToolCallbackProvider;
import org.springdoc.ai.mcp.OpenApiToolCallback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests that {@link McpToolCustomizer} can override the global safe/mutating
 * classification per endpoint via {@code context.setSafeEndpoint(Boolean)}.
 *
 * @author bnasslahsen
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestPropertySource(properties = { "springdoc.ai.mcp.enabled=true", "springdoc.ai.mcp.init-timeout-seconds=30",
		"springdoc.pre-loading-enabled=true", "spring.main.lazy-initialization=false",
		"springdoc.ai.mcp.guardrails.require-approval-for-mutating-tools=true" })
class McpEndpointSafetyOverrideTest {

	/**
	 * The tool callback provider.
	 */
	@Autowired
	private OpenApiMcpToolCallbackProvider toolCallbackProvider;

	/**
	 * The test application with a customizer that overrides the safety classification of
	 * two endpoints against the global default.
	 */
	@SpringBootApplication
	static class TestApp {

		/**
		 * Registers a customizer that overrides POST /reports/generate to safe and GET
		 * /cache/invalidate to mutating, leaving all other endpoints at their global
		 * default.
		 * @return the MCP tool customizer
		 */
		@Bean
		McpToolCustomizer safetyOverrideCustomizer() {
			return (context, path, method, operation) -> {
				// POST is normally mutating — override to safe (read-like report query)
				if (method == PathItem.HttpMethod.POST && "/reports/generate".equals(path)) {
					context.setSafeEndpoint(true);
				}
				// GET is normally safe — override to mutating (cache invalidation is a side-effect)
				else if (method == PathItem.HttpMethod.GET && "/cache/invalidate".equals(path)) {
					context.setSafeEndpoint(false);
				}
				return context;
			};
		}

	}

	/**
	 * Verifies that per-endpoint overrides from {@code setSafeEndpoint} take precedence
	 * over the global {@code safe-methods} configuration, while endpoints without an
	 * override continue to use the global default.
	 */
	@Test
	void safeEndpointOverridesTakePrecedenceOverGlobalConfig() {
		Map<String, OpenApiToolCallback> callbacks = Arrays.stream(toolCallbackProvider.getToolCallbacks())
			.filter(OpenApiToolCallback.class::isInstance)
			.map(OpenApiToolCallback.class::cast)
			.collect(Collectors.toMap(cb -> cb.getToolDefinition().name(), cb -> cb));

		// POST /reports/generate — overridden to safe, so HITL is skipped
		assertThat(callbacks.get("generate_report").isSafe()).isTrue();
		assertThat(callbacks.get("generate_report").isRequiresApproval()).isFalse();

		// GET /cache/invalidate — overridden to mutating, so HITL applies despite GET
		assertThat(callbacks.get("invalidate_cache").isSafe()).isFalse();
		assertThat(callbacks.get("invalidate_cache").isRequiresApproval()).isTrue();

		// GET /items — no override, global default applies (GET = safe)
		assertThat(callbacks.get("list_items").isSafe()).isTrue();
		assertThat(callbacks.get("list_items").isRequiresApproval()).isFalse();

		// DELETE /items/{id} — no override, global default applies (DELETE = mutating)
		assertThat(callbacks.get("delete_item").isSafe()).isFalse();
		assertThat(callbacks.get("delete_item").isRequiresApproval()).isTrue();
	}

}
