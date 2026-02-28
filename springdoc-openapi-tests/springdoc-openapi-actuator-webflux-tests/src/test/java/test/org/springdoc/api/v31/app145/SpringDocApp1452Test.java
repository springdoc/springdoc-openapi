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
import test.org.springdoc.api.v31.AbstractSpringDocActuatorTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import static org.junit.jupiter.api.Assertions.fail;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;


@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT,
		properties = { "management.endpoints.web.exposure.include=*",
				"server.port=53595",
				"springdoc.use-management-port=true",
				"springdoc.group-configs[0].group=users",
				"springdoc.group-configs[0].packages-to-scan=test.org.springdoc.api.v31.app145",
				"management.server.port=9385",
				"management.endpoints.web.base-path=/application" })
public class SpringDocApp1452Test extends AbstractSpringDocActuatorTest {

	@Test
	void testApp() throws Exception {
		try {
			String result = webClient.get().uri("/application/openapi/users").retrieve()
					.bodyToMono(String.class).block();
			String expected = getContent("results/3.1.0/app145-2.json");
			assertEquals(expected, result, true);
		}
		catch (WebClientResponseException ex) {
			fail();
		}
	}

	@SpringBootApplication
	static class SpringDocTestApp {}

}
