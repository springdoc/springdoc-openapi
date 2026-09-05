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

import java.util.concurrent.atomic.AtomicReference;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests the redaction of secret-shaped values in {@link McpAuditLogger} events.
 *
 * @author bnasslahsen
 */
class McpAuditLoggerTest {

	/**
	 * The object mapper.
	 */
	private final ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * Captures the last serialized audit event.
	 */
	private final AtomicReference<String> captured = new AtomicReference<>();

	@BeforeEach
	void registerSink() {
		McpAuditLogger.setRedactionEnabled(true);
		McpAuditLogger.setEventSink(captured::set);
	}

	@AfterEach
	void unregisterSink() {
		McpAuditLogger.setEventSink(null);
		McpAuditLogger.setRedactionEnabled(true);
	}

	/**
	 * Logs a record carrying secret-shaped arguments, URL parameters and bodies.
	 * @return the parsed {@code translation} node of the emitted event
	 * @throws Exception if JSON parsing fails
	 */
	private JsonNode logSecretRecord() throws Exception {
		McpAuditLogger.log(McpAuditLogger.AuditRecord.builder()
			.toolName("create_token")
			.httpMethod("POST")
			.pathPattern("/tokens")
			.outcomeStatus("SUCCESS")
			.mcpArguments("{\"user\":\"alice\",\"password\":\"s3cr3t\","
					+ "\"nested\":{\"X-API-Key\":\"abc\",\"keep\":\"visible\"}}")
			.requestUrl("http://localhost:8080/tokens?user=alice&access_token=leaked")
			.requestBody("{\"clientSecret\":\"shhh\",\"scope\":\"read\"}")
			.responseBody("{\"accessToken\":\"jwt-value\",\"expiresIn\":3600}")
			.build());
		return objectMapper.readTree(captured.get()).path("translation");
	}

	/**
	 * Secret-shaped argument fields are masked, at any nesting depth, while other fields
	 * stay readable.
	 * @throws Exception if JSON parsing fails
	 */
	@Test
	void testSecretArgumentsAreRedacted() throws Exception {
		JsonNode translation = logSecretRecord();
		assertThat(translation.path("mcp_arguments").path("password").asText()).isEqualTo("***");
		assertThat(translation.path("mcp_arguments").path("nested").path("X-API-Key").asText()).isEqualTo("***");
		assertThat(translation.path("mcp_arguments").path("user").asText()).isEqualTo("alice");
		assertThat(translation.path("mcp_arguments").path("nested").path("keep").asText()).isEqualTo("visible");
		assertThat(captured.get()).doesNotContain("s3cr3t", "abc");
	}

	/**
	 * Secret-shaped query parameters are masked while the rest of the URL is preserved.
	 * @throws Exception if JSON parsing fails
	 */
	@Test
	void testSecretQueryParametersAreRedacted() throws Exception {
		JsonNode translation = logSecretRecord();
		assertThat(translation.path("request_url").asText())
			.isEqualTo("http://localhost:8080/tokens?user=alice&access_token=***");
	}

	/**
	 * Secret-shaped fields of the request and response bodies are masked.
	 * @throws Exception if JSON parsing fails
	 */
	@Test
	void testSecretBodyFieldsAreRedacted() throws Exception {
		JsonNode translation = logSecretRecord();
		assertThat(translation.path("request_body").path("clientSecret").asText()).isEqualTo("***");
		assertThat(translation.path("request_body").path("scope").asText()).isEqualTo("read");
		assertThat(objectMapper.readTree(translation.path("response_body").asText()).path("accessToken").asText())
			.isEqualTo("***");
		assertThat(captured.get()).doesNotContain("shhh", "jwt-value");
	}

	/**
	 * Redaction can be turned off for integrators that need the raw payloads.
	 * @throws Exception if JSON parsing fails
	 */
	@Test
	void testRedactionCanBeDisabled() throws Exception {
		McpAuditLogger.setRedactionEnabled(false);
		JsonNode translation = logSecretRecord();
		assertThat(translation.path("mcp_arguments").path("password").asText()).isEqualTo("s3cr3t");
		assertThat(translation.path("request_url").asText()).endsWith("access_token=leaked");
	}

}
