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

package test.org.springdoc.api.v31.app145;

import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springdoc.core.utils.Constants;
import test.org.springdoc.api.v31.AbstractSpringDocActuatorTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientResponseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT,
		properties = { "management.endpoints.web.exposure.include=*",
				"server.port=52556",
				"springdoc.use-management-port=true",
				"springdoc.group-configs[0].group=users",
				"springdoc.group-configs[0].packages-to-scan=test.org.springdoc.api.v31.app145",
				"management.server.port=9291",
				"management.endpoints.web.base-path=/application" })
public class SpringDocApp145Test extends AbstractSpringDocActuatorTest {

	@Test
	void testApp() throws Exception {
		mockMvc.perform(get(Constants.DEFAULT_API_DOCS_URL + "/users"))
				.andExpect(status().isNotFound());
	}

	@Test
	void testApp1() {
		// /application/openapi should be 404
		assertThrows(HttpClientErrorException.NotFound.class, () ->
				actuatorClient.get()
						.uri("/application/openapi")
						.retrieve()
						.body(String.class)
		);
	}

	@Test
	void testApp2() throws Exception {
		String result = actuatorClient.get()
				.uri("/application/openapi/users")
				.retrieve()
				.body(String.class);

		String expected = getContent("results/3.1.0/app145.json");
		// strict JSON comparison
		JSONAssert.assertEquals(expected, result, true);
	}

	@Test
	void testApp3() {
		RestClientResponseException ex = assertThrows(RestClientResponseException.class, () ->
				actuatorClient.get()
						.uri("/application/openapi/{group}", Constants.DEFAULT_GROUP_NAME)
						.retrieve()
						.body(String.class)
		);
		assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
	}

	@SpringBootApplication
	static class SpringDocTestApp {}
}
