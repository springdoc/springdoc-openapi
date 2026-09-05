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

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem.HttpMethod;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.junit.jupiter.api.Test;

import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link OpenApiToolCallback}.
 *
 * @author bnasslahsen
 */
class OpenApiToolCallbackTest {

	/**
	 * The object mapper.
	 */
	private final ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * Builds a callback for {@code GET /users/{id}} with a declared path parameter.
	 * @return the callback
	 */
	private OpenApiToolCallback declaredPathParamCallback() {
		Operation operation = new Operation();
		operation.setOperationId("getUser");
		operation.setParameters(List.of(new Parameter().name("id").in("path").schema(new StringSchema())));
		return new OpenApiToolCallback("/users/{id}", HttpMethod.GET, operation, null, "http://localhost:8080");
	}

	/**
	 * Builds a callback for {@code GET /users/{id}} with no declared parameters, so the
	 * template variable is resolved by the regex fallback.
	 * @return the callback
	 */
	private OpenApiToolCallback undeclaredPathParamCallback() {
		Operation operation = new Operation();
		operation.setOperationId("getUser");
		return new OpenApiToolCallback("/users/{id}", HttpMethod.GET, operation, null, "http://localhost:8080");
	}

	/**
	 * Invokes the private {@code resolvePath} method.
	 * @param callback the callback under test
	 * @param inputJson the tool input JSON
	 * @return the resolved path
	 * @throws Exception if JSON parsing fails
	 */
	private String resolvePath(OpenApiToolCallback callback, String inputJson) throws Exception {
		JsonNode input = objectMapper.readTree(inputJson);
		return ReflectionTestUtils.invokeMethod(callback, "resolvePath", input);
	}

	/**
	 * A declared path parameter containing traversal characters must not escape its path
	 * segment.
	 * @throws Exception if JSON parsing fails
	 */
	@Test
	void testDeclaredPathParameterIsEncoded() throws Exception {
		String resolved = resolvePath(declaredPathParamCallback(), "{\"id\":\"1/../../actuator/env\"}");
		assertThat(resolved).isEqualTo("/users/1%2F%2E%2E%2F%2E%2E%2Factuator%2Fenv");
		assertThat(resolved.substring("/users/".length())).doesNotContain("/", "..");
	}

	/**
	 * A declared path parameter must not be able to append a query string or a fragment.
	 * @throws Exception if JSON parsing fails
	 */
	@Test
	void testDeclaredPathParameterCannotInjectQueryOrFragment() throws Exception {
		String resolved = resolvePath(declaredPathParamCallback(), "{\"id\":\"1?x=y#frag\"}");
		assertThat(resolved).doesNotContain("?", "#");
		assertThat(resolved).isEqualTo("/users/1%3Fx%3Dy%23frag");
	}

	/**
	 * The regex fallback branch must encode undeclared template variables too.
	 * @throws Exception if JSON parsing fails
	 */
	@Test
	void testUndeclaredPathVariableIsEncoded() throws Exception {
		String resolved = resolvePath(undeclaredPathParamCallback(), "{\"id\":\"a b/c\"}");
		assertThat(resolved).isEqualTo("/users/a%20b%2Fc");
	}

	/**
	 * A plain value must be left readable, with no unexpected escaping.
	 * @throws Exception if JSON parsing fails
	 */
	@Test
	void testPlainPathParameterIsUnchanged() throws Exception {
		assertThat(resolvePath(declaredPathParamCallback(), "{\"id\":\"42\"}")).isEqualTo("/users/42");
	}

	/**
	 * A missing path variable resolves to an empty segment.
	 * @throws Exception if JSON parsing fails
	 */
	@Test
	void testMissingPathVariableResolvesToEmpty() throws Exception {
		assertThat(resolvePath(undeclaredPathParamCallback(), "{}")).isEqualTo("/users/");
	}

}
