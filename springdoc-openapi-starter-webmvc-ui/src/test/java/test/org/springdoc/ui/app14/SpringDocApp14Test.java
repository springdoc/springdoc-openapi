/*
 *
 *  * Copyright 2019-2026 the original author or authors.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      https://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package test.org.springdoc.ui.app14;

import org.junit.jupiter.api.Test;
import test.org.springdoc.ui.AbstractSpringDocActuatorTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
		properties = { "management.endpoints.web.exposure.include=*", "springdoc.use-management-port=true",
				"management.server.port=9393", "management.endpoints.web.base-path=/application" })
class SpringDocApp14Test extends AbstractSpringDocActuatorTest {

	@Test
	void testIndex() throws Exception {
		MvcResult mvcResult = mockMvc.perform(get("/application/swagger-ui/index.html")).andExpect(status().isOk()).andReturn();
		String contentAsString = mvcResult.getResponse().getContentAsString();
		assertTrue(contentAsString.contains("Swagger UI"));
	}
	@Test
	void testIndexActuator() {
		String contentAsString = actuatorClient.get()
				.uri("/application/swagger-ui")
				.retrieve()
				.body(String.class);

		assertTrue(contentAsString.contains("Swagger UI"));
	}

	@Test
	void testIndexSwaggerConfig() throws Exception {
		String contentAsString = actuatorClient.get()
				.uri("/application/swagger-ui/swagger-config")
				.retrieve()
				.body(String.class);

		String expected = getContent("results/app14-1.json");

		assertEquals(expected, contentAsString, true); // true = strict comparison
	}

	@SpringBootApplication
	static class SpringDocTestApp {}

}