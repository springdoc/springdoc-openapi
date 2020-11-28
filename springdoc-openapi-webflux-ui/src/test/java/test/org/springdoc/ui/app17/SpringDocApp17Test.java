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

package test.org.springdoc.ui.app17;

import org.junit.jupiter.api.Test;
import test.org.springdoc.ui.AbstractSpringDocActuatorTest;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
		properties = { "management.endpoints.web.exposure.include:*",
				"springdoc.show-actuator=true",
				"management.server.port=9096",
				"management.server.base-path=/test",
				"management.endpoints.web.base-path=/application" })
class SpringDocApp17Test extends AbstractSpringDocActuatorTest {

	@SpringBootApplication
	static class SpringDocTestApp {}

	@Test
	void testIndex() {
		EntityExchangeResult<byte[]> getResult = webTestClient.get().uri("/webjars/swagger-ui/index.html")
				.exchange()
				.expectStatus().isOk()
				.expectBody().returnResult();
		String contentAsString = new String(getResult.getResponseBody());
		assertTrue(contentAsString.contains("Swagger UI"));
	}

}