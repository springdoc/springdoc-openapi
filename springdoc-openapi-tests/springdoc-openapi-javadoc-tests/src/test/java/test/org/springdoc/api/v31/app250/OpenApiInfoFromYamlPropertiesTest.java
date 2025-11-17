/*
 *
 *  *
 *  *  *
 *  *  *  *
 *  *  *  *  *
 *  *  *  *  *  * Copyright 2019-2025 the original author or authors.
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

package test.org.springdoc.api.v31.app250;

import org.junit.jupiter.api.Test;
import org.springdoc.core.utils.Constants;
import test.org.springdoc.api.v31.AbstractSpringDocTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests loading OpenAPI info metadata from YAML configuration.
 */
@TestPropertySource(properties = "spring.config.additional-location=classpath:/application-openapi-info.yml")
class OpenApiInfoFromYamlPropertiesTest extends AbstractSpringDocTest {

	/**
	 * Verifies the configured info block is propagated to the generated OpenAPI document.
	 *
	 * @throws Exception the exception
	 */
	@Test
	@Override
	protected void testApp() throws Exception {
		mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.info.title", is("Configured API")))
				.andExpect(jsonPath("$.info.description", is("Configured via YAML")))
				.andExpect(jsonPath("$.info.termsOfService", is("https://example.org/terms")))
				.andExpect(jsonPath("$.info.summary", is("High level summary")))
				.andExpect(jsonPath("$.info.version", is("9.9.9")))
				.andExpect(jsonPath("$.info.contact.name", is("Example Support")))
				.andExpect(jsonPath("$.info.contact.email", is("support@example.org")))
				.andExpect(jsonPath("$.info.contact.url", is("https://example.org/support")))
				.andExpect(jsonPath("$.info.license.name", is("Example License")))
				.andExpect(jsonPath("$.info.license.url", is("https://example.org/license")))
				.andExpect(jsonPath("$.info['x-company']", is("Example Corp")))
				.andExpect(jsonPath("$.info['x-service-tier']", is("gold")));
	}

	/**
	 * Minimal application with one endpoint to ensure the OpenAPI doc is generated.
	 */
	@SpringBootApplication
	@RestController
	static class SpringDocTestApp {

		/**
		 * Sample endpoint to register at least one route.
		 *
		 * @return the greeting
		 */
		@GetMapping("/greetings")
		public String greetings() {
			return "hello";
		}
	}
}

