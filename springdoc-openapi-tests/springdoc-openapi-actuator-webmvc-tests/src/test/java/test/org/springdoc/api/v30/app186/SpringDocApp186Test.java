/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  * Copyright 2019-2026 the original author or authors.
 *  *  *  *  *
 *  *  *  *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  *  *  *  * you may not use this file except in compliance with the License.
 *  *  *  *  * You may obtain a copy of the License at
 *  *  *  *  *
 *  *  *  *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *  *  *  *
 *  *  *  *  * Unless required by applicable law or agreed to in writing, software
 *  *  *  *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  *  *  *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  *  *  * See the License for the specific language governing permissions and
 *  *  *  *  * limitations under the License.
 *  *  *  *
 *  *  *
 *  *
 *
 */

package test.org.springdoc.api.v30.app186;

import org.junit.jupiter.api.Test;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.utils.Constants;
import test.org.springdoc.api.v30.AbstractSpringDocTest;

import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;

import static org.hamcrest.Matchers.is;
import static org.springdoc.core.utils.Constants.ALL_PATTERN;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource(properties = { "springdoc.show-actuator=true",
		"springdoc.group-configs[0].group=group-actuator-as-properties",
		"springdoc.group-configs[0].paths-to-match=${management.endpoints.web.base-path:/actuator}/**",
		"management.endpoints.enabled-by-default=true",
		"management.endpoints.web.exposure.include=*",
		"management.endpoints.web.exposure.exclude=functions, shutdown" })
public class SpringDocApp186Test extends AbstractSpringDocTest {

	@Test
	protected void testApp() throws Exception {
		mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL))
				.andExpect(jsonPath("$.openapi", is("3.0.1")))
				.andExpect(status().isOk())
				.andExpect(content().json(getContent("results/3.0.1/app186.json"), true));
	}

	@Test
	void testGroupActuatorAsCodeCheckBackwardsCompatibility() throws Exception {
		mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL + "/group-actuator-as-code-check-backwards-compatibility"))
				.andExpect(jsonPath("$.openapi", is("3.0.1")))
				.andExpect(status().isOk())
				.andExpect(content().json(getContent("results/3.0.1/app186.json"), true));
	}

	@Test
	void testGroupActuatorAsCode() throws Exception {
		mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL + "/group-actuator-as-code"))
				.andExpect(jsonPath("$.openapi", is("3.0.1")))
				.andExpect(status().isOk())
				.andExpect(content().json(getContent("results/3.0.1/app186.json"), true));
	}

	@Test
	void testGroupActuatorAsProperties() throws Exception {
		mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL + "/group-actuator-as-properties"))
				.andExpect(jsonPath("$.openapi", is("3.0.1")))
				.andExpect(status().isOk())
				.andExpect(content().json(getContent("results/3.0.1/app186.json"), true));
	}

	@SpringBootApplication
	static class SpringDocTestApp {

		@Bean
		public GroupedOpenApi asCodeCheckBackwardsCompatibility(OperationCustomizer actuatorCustomizer, WebEndpointProperties endpointProperties) {
			return GroupedOpenApi.builder()
					.group("group-actuator-as-code-check-backwards-compatibility")
					.pathsToMatch(endpointProperties.getBasePath() + ALL_PATTERN)
					.addOperationCustomizer(actuatorCustomizer)
					.build();
		}

		@Bean
		public GroupedOpenApi asCode(WebEndpointProperties endpointProperties) {
			return GroupedOpenApi.builder()
					.group("group-actuator-as-code")
					.pathsToMatch(endpointProperties.getBasePath() + ALL_PATTERN)
					.build();
		}
	}

}