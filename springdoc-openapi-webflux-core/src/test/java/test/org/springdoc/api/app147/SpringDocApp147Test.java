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

package test.org.springdoc.api.app147;

import org.junit.jupiter.api.Test;
import org.springdoc.core.Constants;
import test.org.springdoc.api.AbstractSpringDocActuatorTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
		properties = { "management.endpoints.web.exposure.include:*",
				"springdoc.show-actuator=true",
				"management.server.port=9097",
				"management.server.base-path=/test",
				"management.endpoints.web.base-path=/application" })
public class SpringDocApp147Test  extends AbstractSpringDocActuatorTest {

	@SpringBootApplication
	static class SpringDocTestApp {}


	@Test
	public void testApp() throws Exception {
		EntityExchangeResult<byte[]> getResult =  webTestClient.get().uri(Constants.DEFAULT_API_DOCS_URL + "/"+Constants.ACTUATOR_DEFAULT_GROUP)
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.openapi").isEqualTo("3.0.1")
				.returnResult();
	   String result = new String(getResult.getResponseBody());
	   String expected = getContent("results/app147-1.json");
	   assertEquals(expected, result, true);
	}

	@Test
	public void testApp1() throws Exception {
		EntityExchangeResult<byte[]> getResult =  webTestClient.get().uri(Constants.DEFAULT_API_DOCS_URL + "/users")
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$.openapi").isEqualTo("3.0.1")
				.returnResult();
		String result = new String(getResult.getResponseBody());
		String expected = getContent("results/app147-2.json");
		assertEquals(expected, result, true);
	}


}
