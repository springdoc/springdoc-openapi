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

package test.org.springdoc.api.v30.app144;

import org.junit.jupiter.api.Test;
import org.springdoc.core.utils.Constants;
import test.org.springdoc.api.v30.AbstractSpringDocActuatorTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;


@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT,
		properties = { "management.endpoints.web.exposure.include=*",
				"server.port=52555",
				"springdoc.use-management-port=true",
				"management.server.port=9289",
				"management.endpoints.web.base-path=/application" })
public class SpringDocApp144Test extends AbstractSpringDocActuatorTest {

	@Test
	void testApp() {
		webTestClient.get().uri(Constants.DEFAULT_API_DOCS_URL).exchange()
				.expectStatus().isNotFound();
	}

	@Test
	void testApp1() throws Exception {
		String result = webClient.get().uri("/application/openapi").retrieve()
				.bodyToMono(String.class)
				.block();
		String expected = getContent("results/3.0.1/app144.json");
		assertEquals(expected, result, true);
	}

	@SpringBootApplication
	static class SpringDocTestApp {}

}
