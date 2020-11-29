/*
 *
 *  * Copyright 2019-2020 the original author or authors.
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

package test.org.springdoc.api.app145;

import org.junit.jupiter.api.Test;
import org.springdoc.core.Constants;
import test.org.springdoc.api.AbstractSpringDocActuatorTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;


@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT,
		properties = { "management.endpoints.web.exposure.include:*",
				"server.port=55556",
				"springdoc.use-management-port=true",
				"springdoc.group-configs[0].group=users",
				"springdoc.group-configs[0].packages-to-scan=test.org.springdoc.api.app145",
				"management.server.port=9091",
				"management.endpoints.web.base-path=/application" })
public class SpringDocApp145Test  extends AbstractSpringDocActuatorTest {

	@SpringBootApplication
	static class SpringDocTestApp {}

	@Test
	public void testApp()  {
		webTestClient.get().uri(Constants.DEFAULT_API_DOCS_URL + "/users")
				.exchange()
				.expectStatus().isNotFound();
	}

	@Test
	public void testApp1() {
		try {
			webClient.get().uri("/application/openapi").retrieve()
					.bodyToMono(String.class).block();
			fail();
		}
		catch (WebClientResponseException ex) {
			if (ex.getStatusCode() == HttpStatus.NOT_FOUND)
				assertTrue(true);
			else
				fail();
		}
	}

	@Test
	public void testApp2() throws Exception {
		try {
			String result = webClient.get().uri("/application/openapi/users").retrieve()
					.bodyToMono(String.class).block();
			String expected = getContent("results/app145.json");
			assertEquals(expected, result, true);
		}
		catch (WebClientResponseException ex) {
			fail();
		}
	}

}
