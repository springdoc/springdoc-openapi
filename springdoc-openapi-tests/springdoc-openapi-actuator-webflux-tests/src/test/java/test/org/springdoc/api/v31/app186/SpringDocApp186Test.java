/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  * Copyright 2019-2024 the original author or authors.
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

package test.org.springdoc.api.v31.app186;


import org.junit.jupiter.api.Test;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springdoc.core.utils.Constants;
import test.org.springdoc.api.v31.AbstractCommonTest;

import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;

import static org.springdoc.core.utils.Constants.ALL_PATTERN;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@TestPropertySource(properties = { "springdoc.show-actuator=true",
		"server.port=62138",
		"springdoc.group-configs[0].group=group-actuator-as-properties",
		"springdoc.group-configs[0].paths-to-match=${management.endpoints.web.base-path:/actuator}/**",
		"management.endpoints.enabled-by-default=true",
		"management.endpoints.web.exposure.include=*",
		"management.endpoints.web.exposure.exclude=functions, shutdown" })
public class SpringDocApp186Test extends AbstractCommonTest {

	@Test
	void testApp() throws Exception {
		webTestClient.get().uri(Constants.DEFAULT_API_DOCS_URL).exchange()
				.expectStatus().isOk()
				.expectBody().json(getContent("results/3.1.0/app186.json"), true);
	}

	@Test
	void testGroupActuatorAsCodeCheckBackwardsCompatibility() {
		webTestClient.get().uri(Constants.DEFAULT_API_DOCS_URL + "/group-actuator-as-code-check-backwards-compatibility").exchange()
				.expectStatus().isOk()
				.expectBody().json(getContent("results/3.1.0/app186.json"), true);
	}

	@Test
	void testGroupActuatorAsCode() {
		webTestClient.get().uri(Constants.DEFAULT_API_DOCS_URL + "/group-actuator-as-code").exchange()
				.expectStatus().isOk()
				.expectBody().json(getContent("results/3.1.0/app186.json"), true);
	}

	@Test
	void testGroupActuatorAsProperties() {
		webTestClient.get().uri(Constants.DEFAULT_API_DOCS_URL + "/group-actuator-as-properties").exchange()
				.expectStatus().isOk()
				.expectBody().json(getContent("results/3.1.0/app186.json"), true);
	}

	@SpringBootApplication
	@ComponentScan(basePackages = { "org.springdoc", "test.org.springdoc.api.v31.app186" })
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

