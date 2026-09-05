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

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import io.micrometer.context.ContextRegistry;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springdoc.ai.mcp.McpRequestContextHolder;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import org.springframework.mock.http.server.reactive.MockServerHttpRequest;
import org.springframework.mock.web.server.MockServerWebExchange;
import org.springframework.web.server.WebFilterChain;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests that {@link McpAuditMdcWebFilter} scopes the captured request context to the
 * exchange it belongs to, instead of to whichever event-loop thread ran the filter.
 *
 * @author bnasslahsen
 */
class McpAuditMdcWebFilterTest {

	/**
	 * The filter under test.
	 */
	private final McpAuditMdcWebFilter filter = new McpAuditMdcWebFilter("/mcp");

	@BeforeAll
	static void enableContextPropagation() {
		ContextRegistry.getInstance().registerThreadLocalAccessor(new McpRequestContextAccessor());
		Hooks.enableAutomaticContextPropagation();
	}

	@AfterAll
	static void disableContextPropagation() {
		Hooks.disableAutomaticContextPropagation();
	}

	@AfterEach
	void clearHolder() {
		McpRequestContextHolder.clear();
	}

	/**
	 * Builds an exchange for {@code /mcp} carrying the given bearer token.
	 * @param token the Authorization header value
	 * @return the exchange
	 */
	private MockServerWebExchange mcpExchange(String token) {
		return MockServerWebExchange.from(MockServerHttpRequest.post("/mcp").header("Authorization", token));
	}

	/**
	 * A chain that records the headers visible to a synchronous consumer, after an
	 * asynchronous hop that may switch threads.
	 * @param seen where to record the observed headers
	 * @param delay how long to stay pending before reading
	 * @return the filter chain
	 */
	private WebFilterChain recordingChain(AtomicReference<Map<String, String>> seen, Duration delay) {
		return exchange -> Mono.delay(delay)
			.doOnNext(tick -> seen.set(McpRequestContextHolder.getHeaders()))
			.then();
	}

	/**
	 * Assembling the filter must not write the shared thread local: only the subscription
	 * may see the value, otherwise a concurrent exchange on the same thread observes it.
	 */
	@Test
	void testFilterDoesNotWriteTheThreadLocalOnAssembly() {
		AtomicReference<Map<String, String>> seen = new AtomicReference<>();
		Mono<Void> result = filter.filter(mcpExchange("Bearer a"), recordingChain(seen, Duration.ZERO));

		assertThat(McpRequestContextHolder.getHeaders()).isNull();
		result.block(Duration.ofSeconds(5));
		assertThat(seen.get()).containsEntry("Authorization", "Bearer a");
		assertThat(McpRequestContextHolder.getHeaders()).isNull();
	}

	/**
	 * Two exchanges interleaved on a single shared thread - the situation a Netty event
	 * loop creates - must each observe their own headers.
	 */
	@Test
	void testConcurrentExchangesDoNotShareHeaders() {
		AtomicReference<Map<String, String>> firstSeen = new AtomicReference<>();
		AtomicReference<Map<String, String>> secondSeen = new AtomicReference<>();
		Scheduler sharedThread = Schedulers.newSingle("mcp-test-event-loop");
		try {
			Mono<Void> first = filter.filter(mcpExchange("Bearer first"),
					recordingChain(firstSeen, Duration.ofMillis(200)))
				.subscribeOn(sharedThread);
			Mono<Void> second = filter
				.filter(mcpExchange("Bearer second"), recordingChain(secondSeen, Duration.ofMillis(50)))
				.subscribeOn(sharedThread);

			Mono.when(first, second).block(Duration.ofSeconds(10));
		}
		finally {
			sharedThread.dispose();
		}

		assertThat(firstSeen.get()).containsEntry("Authorization", "Bearer first");
		assertThat(secondSeen.get()).containsEntry("Authorization", "Bearer second");
	}

	/**
	 * Requests outside the MCP endpoint are passed through untouched.
	 */
	@Test
	void testNonMcpRequestsCaptureNothing() {
		AtomicReference<Map<String, String>> seen = new AtomicReference<>();
		MockServerWebExchange exchange = MockServerWebExchange
			.from(MockServerHttpRequest.get("/api/books").header("Authorization", "Bearer a"));

		filter.filter(exchange, recordingChain(seen, Duration.ZERO)).block(Duration.ofSeconds(5));

		assertThat(seen.get()).isNull();
	}

}
