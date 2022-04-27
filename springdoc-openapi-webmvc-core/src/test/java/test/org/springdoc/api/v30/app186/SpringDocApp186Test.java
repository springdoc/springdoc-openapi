/*
 *
 *  *
 *  *  *
 *  *  *  * Copyright 2019-2022 the original author or authors.
 *  *  *  *
 *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  * You may obtain a copy of the License at
 *  *  *  *
 *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *
 *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  * See the License for the specific language governing permissions and
 *  *  *  * limitations under the License.
 *  *  *
 *  *
 *
 */

package test.org.springdoc.api.v30.app186;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.Customization;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.ValueMatcher;
import org.skyscreamer.jsonassert.comparator.CustomComparator;
import org.skyscreamer.jsonassert.comparator.JSONComparator;
import org.springdoc.core.Constants;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.OpenApiCustomiser;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MvcResult;

import test.org.springdoc.api.v30.AbstractSpringDocV30Test;

import static org.springdoc.core.Constants.ALL_PATTERN;

@TestPropertySource(properties = { "springdoc.show-actuator=true",
		"springdoc.group-configs[0].group=group-actuator-as-properties",
		"springdoc.group-configs[0].paths-to-match=${management.endpoints.web.base-path:/actuator}/**",
		"management.endpoints.enabled-by-default=true",
		"management.endpoints.web.exposure.include=*",
		"management.endpoints.web.exposure.exclude=functions, shutdown"})
public class SpringDocApp186Test extends AbstractSpringDocV30Test {

	private static final JSONComparator STRICT_IGNORING_OPERATION_ID = new CustomComparator(JSONCompareMode.STRICT,
			Customization.customization(
					"paths.*.*.operationId"
					, new ValueMatcher<Object>() {
				@Override
				public boolean equal(Object o1, Object o2) {
					return true;
				}
			}));

	@SpringBootApplication
	static class SpringDocTestApp {

		@Bean
		public GroupedOpenApi asCodeCheckBackwardsCompatibility(OpenApiCustomiser actuatorOpenApiCustomiser,
				OperationCustomizer actuatorCustomizer, WebEndpointProperties endpointProperties) {
			return GroupedOpenApi.builder()
					.group("group-actuator-as-code-check-backwards-compatibility")
					.pathsToMatch(endpointProperties.getBasePath()+ ALL_PATTERN)
					.addOpenApiCustomiser(actuatorOpenApiCustomiser)
					.addOperationCustomizer(actuatorCustomizer)
					.build();
		}

		@Bean
		public GroupedOpenApi asCode(WebEndpointProperties endpointProperties) {
			return GroupedOpenApi.builder()
					.group("group-actuator-as-code")
					.pathsToMatch(endpointProperties.getBasePath()+ ALL_PATTERN)
					.build();
		}
	}

	private void assertBodyApp186(MvcResult result) throws Exception {
		String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
		JSONAssert.assertEquals(getContent("results/3.0.1/app186.json"), content, STRICT_IGNORING_OPERATION_ID);
	}

	@Test
	public void testApp() throws Exception {
		mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL))
				.andExpect(jsonPath("$.openapi", is("3.0.1")))
				.andExpect(status().isOk())
				.andExpect(this::assertBodyApp186);
	}

	@Test
	public void testGroupActuatorAsCodeCheckBackwardsCompatibility() throws Exception {
		mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL + "/group-actuator-as-code-check-backwards-compatibility"))
				.andExpect(jsonPath("$.openapi", is("3.0.1")))
				.andExpect(status().isOk())
				.andExpect(this::assertBodyApp186);
	}

	@Test
	public void testGroupActuatorAsCode() throws Exception {
		mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL + "/group-actuator-as-code"))
				.andExpect(jsonPath("$.openapi", is("3.0.1")))
				.andExpect(status().isOk())
				.andExpect(this::assertBodyApp186);
	}

	@Test
	public void testGroupActuatorAsProperties() throws Exception {
		mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL + "/group-actuator-as-properties"))
				.andExpect(jsonPath("$.openapi", is("3.0.1")))
				.andExpect(status().isOk())
				.andExpect(this::assertBodyApp186);
	}

}